package com.app3;

import java.io.IOException;

public class Serveur {
    public static void main(String[] args) throws IOException{
        new ServeurThread().start();
    }
}
