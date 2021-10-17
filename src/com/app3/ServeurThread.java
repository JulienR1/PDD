package com.app3;

import java.io.IOException;

public class ServeurThread extends Thread {

    public ServeurThread() throws IOException{
        this("ServeurThread");
    }

    public ServeurThread(String name) throws IOException{
        super(name);
    }
}
