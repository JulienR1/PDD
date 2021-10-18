package com.app3;

import java.util.Scanner;

public class Menu {
    private ChoixMenu[] choix;

    public Menu(ChoixMenu[] choix) {
        this.choix = choix;
    }

    public int offrir() throws MenuException {
        int tentatives = 0;

        while (true) {
            afficher();

            Scanner scanner = new Scanner(System.in);
            String entreeUtilisateur = scanner.nextLine();

            try {
                int valeurSaisie = Integer.parseInt(entreeUtilisateur);
                valeurSaisie--;

                if (valeurSaisie >= 0 && valeurSaisie < choix.length) {
                    return valeurSaisie;
                }
                tentatives++;
            } catch (NumberFormatException ex) {
                tentatives++;
            } finally {
                if (tentatives > 10) {
                    throw new MenuException("Trop de tentatives echouees dans le menu.");
                }
            }
        }
    }

    private void afficher() {
        System.out.println("--- Menu ---");
        for (int i = 0; i < choix.length; i++) {
            System.out.println((i + 1) + ". " + choix[i].titre);
        }
        System.out.print("> ");
    }
}
