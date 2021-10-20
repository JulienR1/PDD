package com.app3;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
    private final static String FICHIER = "liaisonDeDonnees.log";

    /**
     * Ajout d'un enregistrement textuel dans le fichier de logs.
     * La date et l'heure sont ajoutes automatiquement.
     *
     * @param operation Information a enregistrer.
     */
    public static void enregistrer(String operation) {
        try {
            FileWriter writer = new FileWriter(FICHIER, true);
            writer.append("[" + today() + "]: " + operation + "\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Fonction facilitatrice pour obtenir le moment courant selon le bon format.
     *
     * @return
     */
    private static String today() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
