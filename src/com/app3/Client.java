package com.app3;

import com.app3.couche.CouchePhysique;

import java.nio.charset.StandardCharsets;

public class Client {

    private static ChoixMenu[] choixMenu =
            {
                    new ChoixMenu("Téléverser un fichier", () -> televerser()),
                    new ChoixMenu("Téléverser un fichier (avec erreur)", () -> televerserAvecErreur()),
                    new ChoixMenu("Quitter", () -> quitter())
            };

    public static void main(String[] arg) throws MenuException {
        Menu menu = new Menu(choixMenu);

        System.out.println("PDD - Client");

        boolean isRunning = true;
        while (isRunning) {
            int choix = menu.offrir();
            isRunning = choixMenu[choix].onSelected.get();
        }
    }

    private static boolean televerser() {
        try {
            System.out.println("Televersement en cours");
            CouchePhysique physique = new CouchePhysique("127.0.0.1");
            physique.sendRequete("allo".getBytes(StandardCharsets.UTF_8));
            byte[] caca = physique.getReponse();
            System.out.println(new String(caca, 0, caca.length));
            physique.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    private static boolean televerserAvecErreur() {
        System.out.println("Televersement en cours (avec erreur)");
        return true;
    }

    private static boolean quitter() {
        System.out.println("Fermeture en cours..");
        return false;
    }
}
