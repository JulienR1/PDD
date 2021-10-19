package com.app3.couche;

import com.app3.PDU;

import java.io.FileOutputStream;

public class CoucheApplication extends Couche {
    @Override
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

    public void envoyerFichier(byte[] fichier, String nom) throws Exception {
        handle(new PDU(nom, fichier), false);
    }

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
