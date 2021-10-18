package com.app3;

public class MenuException extends Exception {
    public MenuException(String message) {
        super("MenuException: " + message);
    }
}
