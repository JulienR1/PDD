package com.app3;

public class GestionnaireErreur {
    private static GestionnaireErreur instance = null;

    private boolean estErreur;

    private GestionnaireErreur() {
        this.estErreur = false;
    }

    /**
     * Singleton GestionnaireErreur.
     * Hack pour forcer des erreurs a la couche physique afin de tester les CRCs.
     *
     * @return
     */
    public static GestionnaireErreur Instance() {
        if (GestionnaireErreur.instance == null) {
            GestionnaireErreur.instance = new GestionnaireErreur();
        }
        return GestionnaireErreur.instance;
    }

    /**
     * @return Si une erreur doit etre forcee dans la couche physique.
     */
    public boolean getEstErreur() {
        return estErreur;
    }

    /**
     * @param b L'etat du choix pour les erreurs.
     */
    public void setEstErreur(boolean b) {
        this.estErreur = b;
    }

}
