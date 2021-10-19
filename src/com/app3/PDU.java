package com.app3;

import java.util.Arrays;

public class PDU implements Prototype {
    private String nom;
    private byte[] content;
    private boolean estValide;

    public PDU(String nom, byte[] contenuInitial) {
        this(nom, contenuInitial, true);
    }

    public PDU(String nom, byte[] contenuInitial, boolean estValide) {
        this.nom = nom;
        this.content = contenuInitial;
        this.estValide = estValide;
    }

    public byte[] getBytes() {
        return content.clone();
    }

    public String getNom() {
        return nom;
    }

    public boolean getEstValide() {
        return estValide;
    }

    public void ajouterEntete(byte[] entete) {
        byte[] newContent = new byte[content.length + entete.length];
        System.arraycopy(entete, 0, newContent, 0, entete.length);
        System.arraycopy(content, 0, newContent, entete.length, content.length);
        content = newContent;
    }

    public byte[] enleverEntete(int longueurEntete) throws Exception {
        if (longueurEntete <= 0) {
            // TODO
            throw new Exception("Impossible de retirer une qte negative ou nulle. :)");
        }

        byte[] entete = Arrays.copyOfRange(content, 0, longueurEntete);
        content = Arrays.copyOfRange(content, longueurEntete, content.length);

        return entete;
    }

    public PDU clone() {
        return new PDU(this.getNom(), getBytes(), estValide);
    }
}
