package com.app3.couche;

import com.app3.*;

import java.net.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CouchePhysique extends Couche {

    private DatagramSocket socket;
    private InetAddress adresseIPDestination;
    private int portDestination;

    /**
     * Genere une couche physique qui ecoutera sur le port par defaut.
     * Ce constructeur devrait etre utilise principalement pour la couche physique d'un serveur.
     *
     * @throws SocketException
     * @see Constants
     */
    public CouchePhysique() throws SocketException {
        socket = new DatagramSocket(Constants.DEFAULT_SERVER_PORT);
        initialiserThreadReponses();
    }

    /**
     * Genere une couche physique qui essayera de se connecter a un serveur sur l'ip fourni.
     * Ce constructeur devrait etre utilise principalement pour la couche physique d'un client.
     *
     * @param ipInitiale
     * @throws SocketException
     * @throws CoucheException
     */
    public CouchePhysique(String ipInitiale) throws SocketException, CoucheException {
        setPort(Constants.DEFAULT_SERVER_PORT);
        setIP(ipInitiale);
        socket = new DatagramSocket();
        initialiserThreadReponses();
    }

    private void initialiserThreadReponses() {
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    byte[] reponse = getReponse();
                    handle(new PDU(null, reponse), true);
                } catch (SocketException ex) {
                    System.out.println("Fermeture du socket");
                } catch (TransmissionErrorException e) {
                    System.out.println("Arret du transfert, 3 erreurs");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void handle(PDU pdu, boolean estReception) throws Exception {
        if (estReception) {
            couchePrecedente.handle(pdu, true);
        } else {
            sendRequete(pdu.getBytes());
        }
    }

    private void setPort(int port) {
        this.portDestination = port;
    }

    private void setIP(InetAddress ip) {
        this.adresseIPDestination = ip;
    }

    private void setIP(String ip) throws CoucheException {
        try {
            if (Pattern.matches("\\b\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\b", ip)) {
                setIP(InetAddress.getByName(ip));
            } else {
                throw new CoucheException("L'adresse IP saisie ne respecte pas le format attendu.");
            }
        } catch (UnknownHostException ex) {
            throw new CoucheException(ex.getMessage());
        }
    }

    public byte[] getReponse() throws Exception {
        byte[] buf = new byte[204];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        setIP(packet.getAddress());
        setPort(packet.getPort());
        return Arrays.copyOf(packet.getData(), packet.getLength());
    }

    public void sendRequete(byte[] buf) throws Exception {
        if (buf.length <= 204) {
            if (GestionnaireErreur.Instance().getEstErreur()) {
                int byteToRemove = (int) (Math.random() * (buf.length - 1) + 1);
                buf[byteToRemove] = 0;
                System.out.println("Le byte [" + byteToRemove + "] est maintenant un 0.");
            }

            DatagramPacket packet = new DatagramPacket(buf, buf.length, adresseIPDestination, portDestination);
            socket.send(packet);
        } else {
            throw new Exception("Impossible d'envoyer la requete.");
        }
    }

    @Override
    public void close() {
        socket.close();
        super.close();
    }
}
