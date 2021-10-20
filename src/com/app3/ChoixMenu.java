package com.app3;

import java.util.function.Supplier;

public class ChoixMenu {
    public String titre;
    public Supplier<Boolean> onSelected;

    /**
     * Objet gerant les choix possibles dans un menu. Contient le nom et l'action associe.
     *
     * @param titre
     * @param callback
     */
    public ChoixMenu(String titre, Supplier<Boolean> callback) {
        this.titre = titre;
        this.onSelected = callback;
    }
}
