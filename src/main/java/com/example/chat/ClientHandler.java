package com.example.chat;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private final Socket client;
    private List<String> chat;

    private BufferedReader in;
    private static PrintWriter out;
    private PrintWriter fileOut;

    public ClientHandler(Socket socket, List<String> list) {
        this.client = socket;
        this.chat = list;

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sendUpdateOnlineList();
            }
        }).start();
    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        //For mac - path DESKTOP
        //String name = System.getProperty("user.home") + "/Desktop/" + threadName + ".txt";

        String clientMessage = "";
        try {
            this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            this.out = new PrintWriter(client.getOutputStream(), true);

            //this.fileOut = new PrintWriter(new FileWriter(name), true);
            while (true) {
                clientMessage = this.in.readLine();
                if (clientMessage != null) {
                    if (clientMessage.equals("/exit")) {
                        break;
                    }
                    if (clientMessage.equals("/reset")) {
                        this.chat.clear();
                    } else {
                        this.chat.add(threadName + ": " + clientMessage);
                        this.out.println(this.getChatString());
                        // this.fileOut.println(threadName + ": " + clientMessage);
                        //Notify clients, that the chat got changed
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Close connections
        try {
            this.in.close();
            this.out.close();
            //this.fileOut.close();
            this.client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendUpdateOnlineList() {
        //TODO: DELETE PRINT AFTER ERROR FIXED
        System.out.println("Sending Online Clients");
        for (var client : MessageServer.clientList) {
            clientConnected(client.getSocket(), client.getName());

            if (!client.getThread().isAlive()) {
                clientDisconnected(client.getSocket(), client.getName());
            }
        }
    }

    private static void clientConnected(Socket client, String clientName) {
        out.write("&/" + clientName);
    }

    private static void clientDisconnected(Socket client, String clientName) {
        out.write("/&" + clientName);
    }

    /**
     * Returns a String of the actual chat.
     *
     * @return The chat String.
     */
    public String getChatString() {
        String chatString = "";
        for (String message : this.chat) {
            chatString += message + "\n";
        }
        return chatString;
    }
}
