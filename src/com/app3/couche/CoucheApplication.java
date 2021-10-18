package com.app3.couche;

import com.app3.PDU;

import java.io.FileOutputStream;

public class CoucheApplication implements ICouche {
    @Override
    public void handle(PDU pdu) {
        //TODO
    }

    @Override
    public void setNextCouche(ICouche next) {
        //TODO
    }


    public void envoyerFichier(byte[] fichier, String ipServeur) {
        // TODO
        handle(new PDU(fichier));
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
