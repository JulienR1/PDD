package com.app3;

import com.app3.couche.CouchePhysique;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class ServeurThread extends Thread {

    public ServeurThread() throws IOException {
        this("ServeurThread");
    }

    public ServeurThread(String name) throws IOException {
        super(name);
    }

    public void run() {
        CouchePhysique physique = null;
        try {
            physique = new CouchePhysique();
            while (true) {
                byte[] caca = physique.getReponse();
                System.out.println(new String(caca, 0, caca.length));
                physique.sendRequete("Bonjour, mon beau jeune homme!!!".getBytes(StandardCharsets.UTF_8));
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (physique != null) {
                physique.close();
            }
        }
    }

}
