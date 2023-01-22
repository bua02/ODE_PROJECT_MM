package com.example.chat;

import java.net.Socket;

public class Client {
    private Socket socket;
    private Thread thread;

    public Client(Socket socket, Thread thread) {
        this.socket = socket;
        this.thread = thread;
    }

    public Socket getSocket() {
        return socket;
    }

    public Thread getThread() {
        return thread;
    }

    public String getName() { return this.thread.getName(); }
}
