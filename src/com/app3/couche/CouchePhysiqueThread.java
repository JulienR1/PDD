package com.app3.couche;

import com.app3.PDU;

public class CouchePhysiqueThread extends Thread {

    private CouchePhysique physique;

    public CouchePhysiqueThread(CouchePhysique physique) {
        super("Thread couche physique");
        this.physique = physique;
    }

    public void run() {
        while (true) {
            try {
                // TODO: condition darret pour le thread :)
                byte[] reponse = physique.getReponse();
                physique.handle(new PDU(null, reponse), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
