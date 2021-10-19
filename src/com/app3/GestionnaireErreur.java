package com.app3;

public class GestionnaireErreur {
    private static GestionnaireErreur instance = null;

    private boolean estErreur;

    private GestionnaireErreur() {
        this.estErreur = false;
    }

    public static GestionnaireErreur Instance() {
        if (GestionnaireErreur.instance == null) {
            GestionnaireErreur.instance = new GestionnaireErreur();
        }
        return GestionnaireErreur.instance;
    }

    public boolean getEstErreur() {
        return estErreur;
    }

    public void setEstErreur(boolean b) {
        this.estErreur = b;
    }

}
