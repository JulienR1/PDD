package com.app3.couche;

import com.app3.EnteteTransport;
import com.app3.PDU;
import com.app3.TypeTransmission;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CoucheTransport extends Couche {

    private PDU[] tousLesPaquets;
    private int quantiteErreurs;
    private boolean enTransmission;

    public CoucheTransport() {
        reinitialiserCouche();
    }

    private void reinitialiserCouche() {
        // TODO
        tousLesPaquets = null;
        quantiteErreurs = 0;
        enTransmission = false;
    }

    @Override
    public void handle(PDU _pdu, boolean estReception) throws Exception {
        PDU pdu = _pdu.clone();

        if (estReception) {
            recevoir(pdu);
        } else {
            subdiviserPaquet(pdu);
            transmettrePremier();
        }
    }

    private void transmettrePremier() throws Exception {
        if (!enTransmission) {
            coucheSuivante.handle(tousLesPaquets[0], false);
        } else {
            throw new Exception("Impossible d'envoyer le premier paquet plus d'une fois.");
        }
    }

    private void transmettreContenu() throws Exception {
        if (enTransmission) {
            for (int i = 1; i < tousLesPaquets.length; i++) {
                coucheSuivante.handle(tousLesPaquets[i], false);
            }
        } else {
            throw new Exception("Impossible d'envoyer le contenu avant d'avoir obtenu un accuse-reception du premier paquet.");
        }
    }

    // TODO: verifier si la fonction est appelee plusieurs fois ou juste une fois. je sens quon a ete une tite affaire epa
    private void subdiviserPaquet(PDU pdu) throws Exception {
        byte[] bytes = pdu.getBytes();
        int taillePaquet = 200 - EnteteTransport.GROSSEUR_ENTETE;
        int quantiteDePaquets = (int) Math.ceil(bytes.length / taillePaquet) + 1;

        // Paquet nom du fichier
        PDU paquetNom = new PDU(pdu.getNom(), pdu.getNom().getBytes(StandardCharsets.UTF_8));
        EnteteTransport enteteNom = new EnteteTransport(TypeTransmission.TRANSMISSION, 1, quantiteDePaquets, paquetNom.getBytes().length);
        paquetNom.ajouterEntete(enteteNom.getBytes());
        tousLesPaquets[0] = paquetNom;

        // Paquets contenu
        for (int i = 0; i < bytes.length; i += taillePaquet) {
            byte[] bytesDansPaquet = Arrays.copyOfRange(bytes, i * taillePaquet, Math.min(bytes.length, (i + 1) * taillePaquet));
            PDU paquet = new PDU(pdu.getNom(), bytesDansPaquet);

            EnteteTransport entete = new EnteteTransport(TypeTransmission.TRANSMISSION, i + 1, quantiteDePaquets, paquet.getBytes().length);
            paquet.ajouterEntete(entete.getBytes());

            tousLesPaquets[i + 1] = paquet;
        }
    }

    private void recevoir(PDU paquet) throws Exception {
        EnteteTransport entete = new EnteteTransport(paquet);

        if (entete.getType() == TypeTransmission.ACCUSE_RECEPTION) {
            recevoirAccuseReception(entete.getNumerotation());
        } else if (entete.getType() == TypeTransmission.DEMANDE_RETRANSMISSION) {
            recevoirDemandeRetransmission(entete.getNumerotation());
        } else if (entete.getType() == TypeTransmission.TRANSMISSION) {
            recevoirTransmission(entete, paquet);
        }
    }

    private void recevoirTransmission(EnteteTransport entete, PDU paquet) throws Exception {
        // Quitter si on a deja recu le paquet en question
        if (tousLesPaquets[entete.getNumerotation() - 1] != null) {
            return;
        }

        if (entete.getNumerotation() == 1) {
            recevoirPremierPaquet(entete, paquet);
        } else {
            recevoirPaquetContenu(entete, paquet);
            if (entete.getNumerotation() == tousLesPaquets.length) {
                PDU fichier = reconstruireFichier();
                couchePrecedente.handle(fichier, true);
            }
        }
        accuseReception(entete.getNumerotation());
    }

    private PDU reconstruireFichier() throws Exception {
        String nomFichier = tousLesPaquets[0].getNom();
        int tailleTotale = 0;

        for (int i = 1; i < tousLesPaquets.length; i++) {
            EnteteTransport entete = new EnteteTransport(tousLesPaquets[i]);
            tousLesPaquets[i].enleverEntete(EnteteTransport.GROSSEUR_ENTETE);
            tailleTotale += entete.getTaille();
        }

        int sommeTailleEnCours = 0;
        byte[] contenuFichier = new byte[tailleTotale];
        for (int i = 1; i < tousLesPaquets.length; i++) {
            int tailleEnCours = tousLesPaquets[i].getBytes().length;
            System.arraycopy(tousLesPaquets[i].getBytes(), 0, contenuFichier, sommeTailleEnCours, tailleEnCours);
            sommeTailleEnCours += tailleEnCours;
        }

        return new PDU(nomFichier, contenuFichier);
    }

    private void recevoirPremierPaquet(EnteteTransport entete, PDU paquet) throws Exception {
        tousLesPaquets = new PDU[entete.getQuantitePaquets()];

        paquet.enleverEntete(EnteteTransport.GROSSEUR_ENTETE);
        String nomDuFichier = new String(paquet.getBytes());
        tousLesPaquets[0] = new PDU(nomDuFichier, paquet.getBytes());
    }

    private void recevoirPaquetContenu(EnteteTransport entete, PDU paquet) throws Exception {
        int numeroPaquetPrecedent = entete.getNumerotation() - 1;
        if (!verifierNumerotation(numeroPaquetPrecedent)) {
            demandeRetransmission(numeroPaquetPrecedent);
            quantiteErreurs++;
            if (quantiteErreurs >= 3) {
                reinitialiserCouche();
                // TODO: bon type dexception
                throw new Exception("Connexion perdue car 3 erreurs de transmission! Reinitialisation");
            }
        }

        tousLesPaquets[entete.getNumerotation() - 1] = paquet;
    }

    private void recevoirAccuseReception(int numeroAccuseReception) throws Exception {
        if (numeroAccuseReception == 1) {
            enTransmission = true;
            transmettreContenu();
        }
    }

    private void recevoirDemandeRetransmission(int numeroARetransmettre) throws Exception {
        PDU paquetAEnvoyer = tousLesPaquets[numeroARetransmettre - 1];
        coucheSuivante.handle(paquetAEnvoyer, false);
    }

    private boolean verifierNumerotation(int numerotationAValider) {
        if (numerotationAValider == 1) {
            return true;
        }
        return tousLesPaquets[numerotationAValider] != null;
    }


    private void accuseReception(int numeroRecu) throws Exception {
        EnteteTransport entete = new EnteteTransport(TypeTransmission.ACCUSE_RECEPTION, numeroRecu, tousLesPaquets.length, 0);
        PDU paquetReponse = new PDU(tousLesPaquets[0].getNom(), entete.getBytes());
        coucheSuivante.handle(paquetReponse, false);
    }

    private void demandeRetransmission(int numeroARedemander) throws Exception {
        EnteteTransport entete = new EnteteTransport(TypeTransmission.DEMANDE_RETRANSMISSION, numeroARedemander, tousLesPaquets.length, 0);
        PDU paquetReponse = new PDU(tousLesPaquets[0].getNom(), entete.getBytes());
        coucheSuivante.handle(paquetReponse, false);
    }

    @Override
    public void close() {
        reinitialiserCouche();
        super.close();
    }
}
