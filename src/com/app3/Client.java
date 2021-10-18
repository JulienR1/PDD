package com.app3;

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
        System.out.println("Televersement en cours");
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
