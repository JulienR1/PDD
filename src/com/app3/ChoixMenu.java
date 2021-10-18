package com.app3;

import java.util.function.Supplier;

public class ChoixMenu {
    public String titre;
    public Supplier<Boolean> onSelected;

    public ChoixMenu(String titre, Supplier<Boolean> callback) {
        this.titre = titre;
        this.onSelected = callback;
    }
}
