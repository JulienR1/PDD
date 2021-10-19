package com.app3;

import com.app3.couche.*;

import java.io.IOException;

public class Serveur {
    public static void main(String[] args) throws IOException {
        ICouche app = new CoucheApplication();
        ICouche transport = new CoucheTransport();
        ICouche liaison = new CoucheLiaison();
        ICouche physique = new CouchePhysique();

        app.setCouchesVoisines(null, transport);
        transport.setCouchesVoisines(app, liaison);
        liaison.setCouchesVoisines(transport, physique);
        physique.setCouchesVoisines(liaison, null);

        // Procedure d'arret manuel
        System.out.println("Serveur mis en marche.");
        System.out.println("Appuyer sur entrée pour arreter le serveur...");
        System.in.read();
        app.close();
    }
}
