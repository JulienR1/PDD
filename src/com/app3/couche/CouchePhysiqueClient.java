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

        if(Pattern.matches("\\b\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\b", adresseIPServeur)){
            this.adresseIPServeur = adresseIPServeur;
        }
        else{
            throw new Exception("Entrer une adresse IP valide svp salope");
        }
        socket = new DatagramSocket();
    }
}
