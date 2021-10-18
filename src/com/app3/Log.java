package com.app3;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private final static String FICHIER = "liaisonDeDonnees.log";

    public static void enregistrer(String operation) {
        try {
            FileWriter writer = new FileWriter(FICHIER, true);
            writer.append("[" + today() + "]: " + operation + "\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String today() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
