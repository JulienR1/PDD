package com.app3;

public class CoucheException extends Exception {
    public CoucheException(String message) {
        super("CoucheException: " + message);
    }
}
