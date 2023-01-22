package com.example.chat;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MessageServer {
    public static final int PORT = 4711;
    private ServerSocket server;
    public static List<Client> clientList = new ArrayList<>();
    private List<String> chat = new ArrayList<>();

    /**
     * Starts the server and holds an infinite loop for accepting new clients. Every new client will get a new thread.
     */
    public void startServer() {
        try {
            server = new ServerSocket(PORT);
            System.out.println("Server: Started - waiting for clients");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Infinite loop for accepting clients
        while (true) {
            //Accept client until server gets shutdown
            try {
                var currentSocket = server.accept();
                var currentClient = new Client(currentSocket, new ClientHandler(currentSocket, chat));
                clientList.add(currentClient);

                System.out.println("Server: connected to Client " + currentClient.getSocket().getInetAddress());
                currentClient.getThread().start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        MessageServer ms = new MessageServer();
        ms.startServer();
    }
}
