package com.app3.couche;

import java.net.DatagramSocket;
import java.net.SocketException;

public class CouchePhysiqueServeur extends CouchePhysique{

    /**
     * Couche physique du serveur.
     * @throws SocketException
     */
    public CouchePhysiqueServeur() throws SocketException {
        socket = new DatagramSocket(port);
    }
}
