package com.app3.couche;

import com.app3.EnteteTransport;
import com.app3.PDU;
import com.app3.TransmissionErrorException;
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

    /**
     * Reinitialise les variables de controle de la couche de transport.
     */
    private void reinitialiserCouche() {
        tousLesPaquets = null;
        quantiteErreurs = 0;
        enTransmission = false;
    }

    /**
     * Gere le trafic provenant des couches adjacentes pour le transferer aux fonctions appropriees.
     *
     * @param _pdu
     * @param estReception Gestion de l'orientation du trafic.
     * @throws Exception
     */
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

    /**
     * Premier transfert de paquet pour envoyer le nom du fichier.
     *
     * @throws Exception
     */
    private void transmettrePremier() throws Exception {
        if (!enTransmission) {
            coucheSuivante.handle(tousLesPaquets[0], false);
        } else {
            throw new Exception("Impossible d'envoyer le premier paquet plus d'une fois.");
        }
    }

    /**
     * Transfert de tous les paquets qui composent le fichier.
     *
     * @throws Exception
     */
    private void transmettreContenu() throws Exception {
        if (enTransmission) {
            for (int i = 1; i < tousLesPaquets.length; i++) {
                coucheSuivante.handle(tousLesPaquets[i], false);
            }
            couchePrecedente.handle(null, false);
        } else {
            throw new Exception("Impossible d'envoyer le contenu avant d'avoir obtenu un accuse-reception du premier paquet.");
        }
    }

    /**
     * Separe le contenu du fichier en plus petits paquets de maximumm 200 octets (en-tete inclus)
     * pour transmettre par la suite. Cette operation n'ext executee qu'une seule fois.
     *
     * @param pdu
     * @throws Exception
     */
    private void subdiviserPaquet(PDU pdu) throws Exception {
        byte[] bytes = pdu.getBytes();
        int taillePaquet = 200 - EnteteTransport.GROSSEUR_ENTETE;
        int quantiteDePaquets = (int) Math.ceil(bytes.length / (double) taillePaquet);
        tousLesPaquets = new PDU[quantiteDePaquets + 1];

        // Paquet nom du fichier
        PDU paquetNom = new PDU(pdu.getNom(), pdu.getNom().getBytes(StandardCharsets.UTF_8));
        EnteteTransport enteteNom = new EnteteTransport(TypeTransmission.TRANSMISSION, 1, tousLesPaquets.length, paquetNom.getBytes().length);
        paquetNom.ajouterEntete(enteteNom.getBytes());
        tousLesPaquets[0] = paquetNom;

        // Paquets contenu
        for (int i = 0; i < quantiteDePaquets; i++) {
            byte[] bytesDansPaquet = Arrays.copyOfRange(bytes, i * taillePaquet, Math.min(bytes.length, (i + 1) * taillePaquet));
            PDU paquet = new PDU(pdu.getNom(), bytesDansPaquet);

            EnteteTransport entete = new EnteteTransport(TypeTransmission.TRANSMISSION, i + 2, tousLesPaquets.length, bytesDansPaquet.length);
            paquet.ajouterEntete(entete.getBytes());

            tousLesPaquets[i + 1] = paquet;
        }
    }

    /**
     * Toute reception de paquet est geree et distribuee selon ses proprietes.
     *
     * @param paquet
     * @throws Exception
     */
    private void recevoir(PDU paquet) throws Exception {
        EnteteTransport entete = new EnteteTransport(paquet);
        PDU clone = paquet.clone();
        clone.enleverEntete(EnteteTransport.GROSSEUR_ENTETE);


        if (entete.getType() == TypeTransmission.ACCUSE_RECEPTION) {
            recevoirAccuseReception(entete.getNumerotation());
        } else if (entete.getType() == TypeTransmission.DEMANDE_RETRANSMISSION) {
            recevoirDemandeRetransmission(entete.getNumerotation());
        } else if (entete.getType() == TypeTransmission.TRANSMISSION) {
            recevoirTransmission(entete, paquet);
        }
    }

    /**
     * Reception d'un paquet de type TRANSMISSION. Contient l'information pertinente au fichier a transferer.
     *
     * @param entete Proprietes associes au paquet.
     * @param paquet Contenu en bytes du paquet.
     * @throws Exception
     */
    private void recevoirTransmission(EnteteTransport entete, PDU paquet) throws Exception {
        // Quitter si on a deja recu le paquet en question
        if (tousLesPaquets != null && tousLesPaquets[entete.getNumerotation() - 1] != null) {
            return;
        }

        if (!paquet.getEstValide()) {
            augmenterQuantiteErreurs();
            demandeRetransmission(entete.getNumerotation());
            return;
        }

        boolean finTransmission = false;
        if (entete.getNumerotation() == 1) {
            recevoirPremierPaquet(entete, paquet);
        } else if (tousLesPaquets != null) {
            recevoirPaquetContenu(entete, paquet);
            if (entete.getNumerotation() == tousLesPaquets.length) {
                PDU fichier = reconstruireFichier();
                couchePrecedente.handle(fichier, true);
                finTransmission = true;
            }
        }
        accuseReception(entete.getNumerotation());

        if (finTransmission) {
            reinitialiserCouche();
        }
    }

    /**
     * Initialise le transfert de l'information.
     *
     * @param entete Proprietes associees au paquet.
     * @param paquet Contenu du paquet en bytes. Contient le nom du fichier.
     * @throws Exception
     */
    private void recevoirPremierPaquet(EnteteTransport entete, PDU paquet) throws Exception {
        tousLesPaquets = new PDU[entete.getQuantitePaquets()];

        paquet.enleverEntete(EnteteTransport.GROSSEUR_ENTETE);
        String nomDuFichier = new String(paquet.getBytes());
        tousLesPaquets[0] = new PDU(nomDuFichier, paquet.getBytes());
    }

    /**
     * Reception de l'information partielle du fichier en transmission.
     *
     * @param entete Proprietes associees au paquet.
     * @param paquet Contenu du paquet en bytes.
     * @throws Exception
     */
    private void recevoirPaquetContenu(EnteteTransport entete, PDU paquet) throws Exception {
        int numeroPaquetPrecedent = entete.getNumerotation() - 1;
        if (!verifierNumerotation(numeroPaquetPrecedent)) {
            demandeRetransmission(numeroPaquetPrecedent);
            augmenterQuantiteErreurs();
        }

        tousLesPaquets[entete.getNumerotation() - 1] = paquet;
    }

    /**
     * Traitement des accuses de reception. Seul le premier paquet est pertinent pour confirmer le debut du transfert du fichier.
     *
     * @param numeroAccuseReception
     * @throws Exception
     */
    private void recevoirAccuseReception(int numeroAccuseReception) throws Exception {
        if (numeroAccuseReception == 1) {
            enTransmission = true;
            transmettreContenu();
        }
    }

    /**
     * Traitement des demandes de retransmission. Envoie a nouveau le paquet si celui-ci existe.
     *
     * @param numeroARetransmettre
     * @throws Exception
     */
    private void recevoirDemandeRetransmission(int numeroARetransmettre) throws Exception {
        PDU paquetAEnvoyer = tousLesPaquets[numeroARetransmettre - 1];
        coucheSuivante.handle(paquetAEnvoyer, false);
    }

    /**
     * A la fin de la reception des paquets, combiner toutes les donnees et le nom du fichier ensembles
     * pour former un objet coherent.
     *
     * @return Un objet contenant toutes les informations necessaires pour reconstruire un fichier.
     * @throws Exception
     */
    private PDU reconstruireFichier() throws Exception {
        String nomFichier = tousLesPaquets[0].getNom();
        int[] tailles = new int[tousLesPaquets.length];
        int tailleTotale = 0;

        for (int i = 1; i < tousLesPaquets.length; i++) {
            EnteteTransport entete = new EnteteTransport(tousLesPaquets[i]);
            tousLesPaquets[i].enleverEntete(EnteteTransport.GROSSEUR_ENTETE);
            tailleTotale += entete.getTaille();
            tailles[i] = entete.getTaille();
        }

        int sommeTailleEnCours = 0;
        byte[] contenuFichier = new byte[tailleTotale];
        for (int i = 1; i < tousLesPaquets.length; i++) {
            System.arraycopy(tousLesPaquets[i].getBytes(), 0, contenuFichier, sommeTailleEnCours, tailles[i]);
            sommeTailleEnCours += tailles[i];
        }

        return new PDU(nomFichier, contenuFichier);
    }

    /**
     * Valide si un paquet a ete recu ou non.
     *
     * @param numerotationAValider
     * @return
     */
    private boolean verifierNumerotation(int numerotationAValider) {
        if (numerotationAValider == 1) {
            return true;
        }
        return tousLesPaquets[numerotationAValider - 1] != null;
    }

    /**
     * Envoie un accuse de reception pour un numero de paquet donne.
     *
     * @param numeroRecu
     * @throws Exception
     */
    private void accuseReception(int numeroRecu) throws Exception {
        repondre(numeroRecu, TypeTransmission.ACCUSE_RECEPTION);
    }

    /**
     * Envoie une demande de retransmission pour un numero de paquet donne.
     *
     * @param numeroARedemander
     * @throws Exception
     */
    private void demandeRetransmission(int numeroARedemander) throws Exception {
        repondre(numeroARedemander, TypeTransmission.DEMANDE_RETRANSMISSION);
    }

    /**
     * Envoie d'une reponse generique.
     *
     * @param numeroPaquet Paquet en question dans la reponse.
     * @param type         Type de transmission a effectuer.
     * @throws Exception
     */
    private void repondre(int numeroPaquet, TypeTransmission type) throws Exception {
        EnteteTransport entete = new EnteteTransport(type, numeroPaquet, tousLesPaquets != null ? tousLesPaquets.length : 0, 0);
        coucheSuivante.handle(new PDU(null, entete.getBytes()), false);
    }

    /**
     * Augmente la quantite d'erreurs survenues.
     *
     * @throws TransmissionErrorException Si 3 erreurs ou plus
     */
    private void augmenterQuantiteErreurs() throws TransmissionErrorException {
        quantiteErreurs++;
        if (quantiteErreurs >= 3) {
            reinitialiserCouche();
            throw new TransmissionErrorException("Connexion perdue car 3 erreurs de transmission! Reinitialisation");
        }
    }

    @Override
    /**
     * Ferme et reinitialise la couche.
     */
    public void close() {
        reinitialiserCouche();
        super.close();
    }
}
