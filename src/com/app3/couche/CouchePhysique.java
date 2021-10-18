package com.app3.couche;

import com.app3.PDU;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.function.ToDoubleBiFunction;
import java.util.regex.Pattern;

public abstract class CouchePhysique implements ICouche{

    private InetAddress adresseIPDestination;

    protected DatagramSocket socket;

    final protected int port = 32035;

    @Override
    public void handle(PDU pdu) {
        //TODO
    }

    @Override
    public void setNextCouche(ICouche next) {
        //TODO
    }

    protected void setIP(InetAddress ip) throws Exception {
        this.adresseIPDestination = ip;
    }

    protected void setIP(String ip) throws Exception {
        if(Pattern.matches("\\b\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}\\b", ip)){
            setIP(InetAddress.getByName(ip));
        }
        else{
            throw new Exception("Entrer une adresse IP valide svp salope");
        }
    }


    public byte[] getReponse () throws Exception {
        byte[]buf = new byte[200];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        setIP(packet.getAddress());
        return Arrays.copyOf(packet.getData(),packet.getLength());
    }
    public void sendRequete (byte[]buf ) throws Exception {
        if(buf.length <= 200){
            DatagramPacket packet = new DatagramPacket(buf, buf.length, adresseIPDestination, port);
            socket.send(packet);
        }else{
            throw new Exception("Mange dla marde");
        }

    }

    @Override
    public void close() {
        socket.close();
    }
}
