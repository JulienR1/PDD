package com.app3.couche;

import com.app3.PDU;

import java.io.FileOutputStream;

public class CoucheApplication implements ICouche {
    @Override
    public void handle(PDU pdu) throws Exception {
        //TODO
    }

    @Override
    public void setNextCouche(ICouche next) {
        //TODO
    }


    public void envoyerFichier(byte[] fichier, String nom) throws Exception {
        // TODO
        handle(new PDU(nom, fichier));
    }


    public void sauvegarderFichier(byte[] contenuFichier, String nom) {
        try {
            FileOutputStream outputStream = new FileOutputStream("reception/" + nom);
            outputStream.write(contenuFichier);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        //TODO
    }
}
