package com.app3;

import java.util.Arrays;

public class PDU extends Prototype {
    private byte[] content;

    public byte[] getBytes() {
        return content.clone();
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

    @Override
    public PDU clone() {
        return (PDU) super.clone();
    }
}
