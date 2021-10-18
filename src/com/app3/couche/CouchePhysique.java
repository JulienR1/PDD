package com.app3.couche;

import com.app3.PDU;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.regex.Pattern;

public abstract class CouchePhysique implements ICouche{

    protected String adresseIPServeur;

    protected DatagramSocket socket;

    @Override
    public void handle(PDU pdu) {

    }

    @Override
    public void setNextCouche(ICouche next) {

    }

    public byte[] lirePaquet () throws IOException {
        byte[]buf = new byte[200];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return packet.getData();
    }
    public byte[] ecrirePaquet () throws IOException {
        byte[]buf = new byte[200];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        return packet.getData();
    }
}
