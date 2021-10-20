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

    /**
     * Objet facilitateur pour generer un en-tete conforme pour la couche transport.
     *
     * @param type            Type de paquet.
     * @param numerotation    Identifiant du paquet en cours.
     * @param quantitePaquets Quantite de paquets dans le message au complet.
     * @param tailleOctets    Taille du paquet en cours en bytes.
     * @throws Exception
     */
    public EnteteTransport(TypeTransmission type, int numerotation, int quantitePaquets, int tailleOctets) throws Exception {
        if (tailleOctets > 200 - GROSSEUR_ENTETE) {
            throw new Exception("Taille maximale de 200 octets (en-tete compris) depassee.");
        }

        entete = new byte[GROSSEUR_ENTETE];
        byte[] paddedTypeBytes = new byte[8];
        byte[] typeBytes = EnteteTransport.types.get(type).getBytes(StandardCharsets.UTF_8);
        System.arraycopy(typeBytes, 0, paddedTypeBytes, 0, Math.min(typeBytes.length, 8));

        System.arraycopy(paddedTypeBytes, 0, entete, 0, 8);
        System.arraycopy(intToBytes(numerotation), 0, entete, 8, 4);
        System.arraycopy(intToBytes(quantitePaquets), 0, entete, 12, 4);
        System.arraycopy(intToBytes(tailleOctets), 0, entete, 16, 4);
    }

    /**
     * Objet facilitateur pour decoder l'en-tete recu.
     *
     * @param paquet
     * @throws Exception
     */
    public EnteteTransport(PDU paquet) throws Exception {
        PDU clone = paquet.clone();
        entete = clone.enleverEntete(GROSSEUR_ENTETE);
        type = new String(Arrays.copyOfRange(entete, 0, 8)).replaceAll("([^A-Za-z])", "");
        numerotation = ByteBuffer.wrap(Arrays.copyOfRange(entete, 8, 12)).getInt();
        quantitePaquets = ByteBuffer.wrap(Arrays.copyOfRange(entete, 12, 16)).getInt();
        taille = ByteBuffer.wrap(Arrays.copyOfRange(entete, 16, 20)).getInt();
    }

    /**
     * @return Les bytes contenues dans l'en-tete.
     */
    public byte[] getBytes() {
        return entete;
    }

    /**
     * @return Le type de paquet.
     */
    public TypeTransmission getType() {
        for (Map.Entry<TypeTransmission, String> entry : types.entrySet()) {
            if (entry.getValue().equals(type)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * @return L'identifiant du paquet en cours.
     */
    public int getNumerotation() {
        return numerotation;
    }

    /**
     * @return Le nombre de paquets dans la transmission.
     */
    public int getQuantitePaquets() {
        return quantitePaquets;
    }

    /**
     * @return La grosseur du paquet en cours en bytes.
     */
    public int getTaille() {
        return taille;
    }

    /**
     * Fonction facilitatrice pour convertir un entier en octets.
     *
     * @param num
     * @return
     */
    private byte[] intToBytes(int num) {
        return ByteBuffer.allocate(4).putInt(num).array();
    }


}
