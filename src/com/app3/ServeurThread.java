package com.app3;

import com.app3.couche.CouchePhysiqueServeur;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServeurThread extends Thread {

    public ServeurThread() throws IOException{
        this("ServeurThread");
    }

    public ServeurThread(String name) throws IOException{
        super(name);
        CouchePhysiqueServeur physique = new CouchePhysiqueServeur();
        try{
          byte[] caca = physique.getReponse();
          System.out.println(new String(caca,0, caca.length));
          physique.sendRequete("Bonjour, mon beau jeune homme!!!".getBytes(StandardCharsets.UTF_8));
          physique.close();

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    public void run (){
        CouchePhysiqueServeur physique;
        try{
            physique = new CouchePhysiqueServeur();

        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        while(true){
            try{
                byte[] caca = physique.getReponse();
                System.out.println(new String(caca,0, caca.length));
                physique.sendRequete("Bonjour, mon beau jeune homme!!!".getBytes(StandardCharsets.UTF_8));


            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        //physique.close();
    }

}
