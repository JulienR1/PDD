package com.app3.couche;

import com.app3.Constants;

import java.net.DatagramSocket;

public class CouchePhysiqueClient extends CouchePhysique {

    /**
     * Couche physique du client
     *
     * @param adresseIPServeur
     * @throws Exception
     */
    public CouchePhysiqueClient(String adresseIPServeur) throws Exception {
        setPort(Constants.DEFAULT_SERVER_PORT);
        setIP(adresseIPServeur);
        socket = new DatagramSocket();
    }
}
