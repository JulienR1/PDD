package com.app3;

import com.app3.couche.*;

import java.net.SocketException;

public class Serveur {
    public static void main(String[] args) throws SocketException {
        ICouche app = new CoucheApplication();
        ICouche transport = new CoucheTransport();
        ICouche liaison = new CoucheLiaison();
        ICouche physique = new CouchePhysique();

        app.setCouchesVoisines(null, transport);
        transport.setCouchesVoisines(app, liaison);
        liaison.setCouchesVoisines(transport, physique);
        physique.setCouchesVoisines(liaison, null);

        System.out.println("Serveur mis en marche.");

        // TODO: procedure d'arret SHUTDOWN
    }
}
