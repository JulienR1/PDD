package com.app3.stats;

import java.util.*;

public class Statistiques {
    private static Statistiques instance = null;

    private Map<StatRecord, Integer> stats;

    private Statistiques() {
        stats = new HashMap<StatRecord, Integer>();
    }

    public static Statistiques Instance() {
        if (Statistiques.instance == null) {
            Statistiques.instance = new Statistiques();
        }
        return Statistiques.instance;
    }

    public void augmenterEnregistrement(StatRecord enregistrement) {
        stats.put(enregistrement, stats.get(enregistrement) != null ? stats.get(enregistrement) + 1 : 0);
    }
}
