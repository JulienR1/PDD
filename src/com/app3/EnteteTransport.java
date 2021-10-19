package com.app3;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EnteteTransport {
    public static final int GROSSEUR_ENTETE = 20;

    private static final HashMap<TypeTransmission, String> types;

    static {
        types = new HashMap<>();
        types.put(TypeTransmission.TRANSMISSION, "ENVOI");
        types.put(TypeTransmission.ACCUSE_RECEPTION, "RECU");
        types.put(TypeTransmission.DEMANDE_RETRANSMISSION, "RETRANS");
    }

    private String type;
    private int numerotation;
    private int quantitePaquets;
    private int taille;

    private byte[] entete;

    public EnteteTransport(TypeTransmission type, int numerotation, int quantitePaquets, int tailleOctets) throws Exception {
        if (tailleOctets > 180) {
            throw new Exception("Taille maximale de 200 octets (en-tete compris) depassee.");
        }

        entete = new byte[GROSSEUR_ENTETE];
        byte[] paddedTypeBytes = new byte[8];
        byte[] typeBytes = EnteteTransport.types.get(type).getBytes(StandardCharsets.UTF_8);
        System.arraycopy(typeBytes, 0, paddedTypeBytes, 0, Math.min(typeBytes.length, 8));

        System.arraycopy(paddedTypeBytes, 0, entete, 0, 8);
        System.arraycopy(intToBytes(numerotation), 0, entete, 8, 4);
        System.arraycopy(intToBytes(quantitePaquets), 0, entete, 12, 4);
        System.arraycopy(intToBytes(tailleOctets + GROSSEUR_ENTETE), 0, entete, 16, 4);
    }

    public EnteteTransport(PDU paquet) throws Exception {
        PDU clone = paquet.clone();
        entete = clone.enleverEntete(GROSSEUR_ENTETE);
        type = new String(Arrays.copyOfRange(entete, 0, 8)).replaceAll("([^A-Za-z])", "");
        numerotation = ByteBuffer.wrap(Arrays.copyOfRange(entete, 8, 12)).getInt();
        quantitePaquets = ByteBuffer.wrap(Arrays.copyOfRange(entete, 12, 16)).getInt();
        taille = ByteBuffer.wrap(Arrays.copyOfRange(entete, 16, 20)).getInt();
    }

    public byte[] getBytes() {
        return entete;
    }

    public TypeTransmission getType() {
        for (Map.Entry<TypeTransmission, String> entry : types.entrySet()) {
            if (entry.getValue().equals(type)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public int getNumerotation() {
        return numerotation;
    }

    public int getQuantitePaquets() {
        return quantitePaquets;
    }

    public int getTaille() {
        return taille;
    }

    private byte[] intToBytes(int num) {
        return ByteBuffer.allocate(4).putInt(num).array();
    }


}
