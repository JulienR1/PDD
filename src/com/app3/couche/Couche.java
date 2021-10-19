package com.app3.couche;

public abstract class Couche implements ICouche {
    protected ICouche couchePrecedente;
    protected ICouche coucheSuivante;

    @Override
    /**
     * La couche precedente est plus proche de l'utilisateur.
     * La couche suivante est plus proche du cote physique.
     * De precedent vers suivant: Application -> Transport -> Liaison -> Physique.
     */
    public void setCouchesVoisines(ICouche precedente, ICouche suivante) {
        this.couchePrecedente = precedente;
        this.coucheSuivante = suivante;
    }

    @Override
    public void close() {
        if (coucheSuivante != null) {
            coucheSuivante.close();
        }
    }
}
