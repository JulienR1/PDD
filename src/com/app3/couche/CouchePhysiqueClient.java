package com.app3.couche;

import java.net.DatagramSocket;
import java.util.regex.Pattern;

public class CouchePhysiqueClient extends CouchePhysique{

    /**
     * Couche physique du client
     * @param adresseIPServeur
     * @throws Exception
     */
    public CouchePhysiqueClient(String adresseIPServeur) throws Exception {
        setIP(adresseIPServeur);
        socket = new DatagramSocket();
    }
}
