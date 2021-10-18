package com.app3.couche;

import com.app3.Constants;

import java.net.DatagramSocket;
import java.net.SocketException;

public class CouchePhysiqueServeur extends CouchePhysique {

    /**
     * Couche physique du serveur.
     *
     * @throws SocketException
     */
    public CouchePhysiqueServeur() throws SocketException {
        socket = new DatagramSocket(Constants.DEFAULT_SERVER_PORT);
    }
}
