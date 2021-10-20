package com.app3;

import java.util.Arrays;

public class PDU implements Prototype {
    private String nom;
    private byte[] content;
    private boolean estValide;

    /**
     * Constructeur normal de PDU.
     * @param nom
     * @param contenuInitial
     */
    public PDU(String nom, byte[] contenuInitial) {
        this(nom, contenuInitial, true);
    }

    /**
     * Constructeur de PDU lors du televersement avec erreurs.
     * @param nom
     * @param contenuInitial
     * @param estValide
     */
    public PDU(String nom, byte[] contenuInitial, boolean estValide) {
        this.nom = nom;
        this.content = contenuInitial;
        this.estValide = estValide;
    }

    /**
     *
     * @return Le contenu (donnees) du PDU dans un tableau de byte.
     */
    public byte[] getBytes() {
        return content.clone();
    }

    /**
     *
     * @return Le nom du fichier qui est televerser.
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @return La validite (avec erreur ou non) du PDU.
     */
    public boolean getEstValide() {
        return estValide;
    }

    /**
     * Ajoute un entete aux PDU. En ajoutant l'entete a gauche du contenu dans un tableau de byte.
     * @param entete
     */
    public void ajouterEntete(byte[] entete) {
        byte[] newContent = new byte[content.length + entete.length];
        System.arraycopy(entete, 0, newContent, 0, entete.length);
        System.arraycopy(content, 0, newContent, entete.length, content.length);
        content = newContent;
    }

    /**
     * Retire un nombre(longueurEntete) de bytes a gauche du tableau de byte du PDU.
     * @param longueurEntete
     * @return Le contenu sans l'entete a retirer (tableau de byte) du PDU.
     * @throws Exception
     */
    public byte[] enleverEntete(int longueurEntete) throws Exception {
        if (longueurEntete <= 0) {
            // TODO
            throw new Exception("Impossible de retirer une qte negative ou nulle. :)");
        }

        byte[] entete = Arrays.copyOfRange(content, 0, longueurEntete);
        content = Arrays.copyOfRange(content, longueurEntete, content.length);

        return entete;
    }

    /**
     * Clone l'instance d'un PDU.
     * @return Un nouveau PDU avec les memes valeurs.
     */
    public PDU clone() {
        return new PDU(this.getNom(), getBytes(), estValide);
    }
}
