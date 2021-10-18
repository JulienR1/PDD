package com.app3;

import com.app3.couche.CoucheApplication;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {

    private static ChoixMenu[] choixMenu =
            {
                    new ChoixMenu("Téléverser un fichier", () -> televerser(false)),
                    new ChoixMenu("Téléverser un fichier (avec erreur)", () -> televerser(true)),
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

    private static boolean televerser(boolean avecErreurs) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Entrer l'adresse IP du serveur:");
            System.out.print("> ");
            String adresseIP = scanner.nextLine();

            System.out.println("Entrer le fichier a televerser:");
            System.out.print("> ");
            String lienFichier = scanner.nextLine();

            byte[] bytesFichier = Files.readAllBytes(Paths.get(lienFichier));
            if (avecErreurs) {
                bytesFichier[(int) (Math.random() * bytesFichier.length)] = 0;
            }

            String[] urlParts = lienFichier.split("\\\\");
            String nomFichier = urlParts[urlParts.length - 1];

            CoucheApplication app = new CoucheApplication();
            // TODO: initialiser chaine de justin ici ou dans les constructeurs, a voir on est lazy
            app.envoyerFichier(bytesFichier, nomFichier);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    private static boolean quitter() {
        System.out.println("Fermeture en cours..");
        return false;
    }
}
