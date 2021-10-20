package com.app3.couche;

import com.app3.PDU;

import java.io.FileOutputStream;

public class CoucheApplication extends Couche {
    @Override
    /**
     * Gestion du trafic provenant des couches adjacentes.
     */
    public void handle(PDU pdu, boolean estReception) throws Exception {
        if (estReception) {
            sauvegarderFichier(pdu.getBytes(), pdu.getNom());
        } else {
            if (pdu != null) {
                coucheSuivante.handle(pdu, false);
            } else {
                close();
            }
        }
    }

    /**
     * Demande d'envoi de fichier vers l'autre application.
     *
     * @param fichier Contenu en bytes du fichier.
     * @param nom     Nom souhaite du fichier.
     * @throws Exception
     */
    public void envoyerFichier(byte[] fichier, String nom) throws Exception {
        handle(new PDU(nom, fichier), false);
    }

    /**
     * Enregistrement du fichier sur l'ordinateur.
     *
     * @param contenuFichier Bytes composant le fichier.
     * @param nom            Nom a assigner au fichier.
     */
    public void sauvegarderFichier(byte[] contenuFichier, String nom) {
        try {
            FileOutputStream outputStream = new FileOutputStream(nom);
            outputStream.write(contenuFichier);
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
