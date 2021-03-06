package com.app3;

import com.app3.couche.*;
import com.app3.stats.Statistiques;

import java.net.SocketException;
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

    /**
     * Porgramme pour envoyer un fichier vers le serveur.
     *
     * @param arg
     * @throws MenuException
     */
    public static void main(String[] arg) throws MenuException {
        Menu menu = new Menu(choixMenu);

        System.out.println("PDD - Client");

        boolean isRunning = true;
        while (isRunning) {
            int choix = menu.offrir();
            isRunning = choixMenu[choix].onSelected.get();
        }
    }

    /**
     * Construit une chaine a double sens pour l'ordre des couches selon le modele OSI/ISO.
     *
     * @param ipServeur
     * @return
     * @throws SocketException
     * @throws CoucheException
     */
    private static CoucheApplication initialiserCouches(String ipServeur) throws SocketException, CoucheException {
        CoucheApplication app = new CoucheApplication();
        ICouche transport = new CoucheTransport();
        ICouche liaison = new CoucheLiaison();
        ICouche physique = new CouchePhysique(ipServeur);

        app.setCouchesVoisines(null, transport);
        transport.setCouchesVoisines(app, liaison);
        liaison.setCouchesVoisines(transport, physique);
        physique.setCouchesVoisines(liaison, null);

        return app;
    }

    /**
     * Selection et envoie d'un fichier vers un serveur specifique.
     *
     * @param avecErreurs Insere manuellement des erreurs aleatoires dans la couche physique.
     * @return
     */
    private static boolean televerser(boolean avecErreurs) {
        GestionnaireErreur.Instance().setEstErreur(avecErreurs);
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Entrer l'adresse IP du serveur:");
            System.out.print("> ");
            String adresseIP = scanner.nextLine();

            System.out.println("Entrer le fichier a televerser:");
            System.out.print("> ");
            String lienFichier = scanner.nextLine();

            byte[] bytesFichier = Files.readAllBytes(Paths.get(lienFichier));

            String[] urlParts = lienFichier.split("\\\\");
            String nomFichier = urlParts[urlParts.length - 1];

            CoucheApplication app = initialiserCouches(adresseIP);
            app.envoyerFichier(bytesFichier, nomFichier);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return true;
    }

    /**
     * Fermeture du programme client.
     *
     * @return
     */
    private static boolean quitter() {
        System.out.println(Statistiques.Instance().toString());
        System.out.println("Fermeture en cours..");
        return false;
    }
}
