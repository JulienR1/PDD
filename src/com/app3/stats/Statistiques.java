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
        stats.put(enregistrement, 1 + (stats.get(enregistrement) != null ? stats.get(enregistrement) : 0));
    }

    @Override
    public String toString() {
        String output = "";

        output += "[\n";
        for (Map.Entry<StatRecord, Integer> entry : stats.entrySet()) {
            output += entry.getKey().toString() + ": " + entry.getValue().toString() + "\n";
        }
        output += "]";

        return output;
    }

}
