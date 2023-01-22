package com.example.chat;

import javafx.scene.chart.PieChart;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MessageServer {
    private ServerSocket server = null;
    private Socket client = null;
    private List<String> chat = new ArrayList<>();

    public MessageServer() {
        this.startServer();
    }

    /**
     * Starts the server and holds an infinite loop for accepting new clients. Every new client will get a new thread.
     */
    public void startServer(){
        try {
            server = new ServerSocket(4711);
            System.out.println("Server: Started - waiting for clients");
        }catch(IOException e){
            e.printStackTrace();
        }
        //Infinite loop for accepting clients
        while (true){
            //Accept client until server gets shutdown
            try{
                client = server.accept();
                System.out.println("Server: connected to Client " + client.getInetAddress());
                Thread thread = new ClientHandler(client, chat);
                thread.start();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MessageServer ms = new MessageServer();
    }
}
