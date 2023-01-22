package com.example.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    public static final int PORT = 4711;
    public static final String LOCALHOST = "localhost";
    @FXML
    private TextField messageInput;
    @FXML
    private TextArea messageOutput;
    @FXML
    private ListView onlineClients;

    private Socket server = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private boolean hasConnection = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Connection has started");
        try {
            Socket client = new Socket(LOCALHOST, PORT);
            this.server = client;
            this.hasConnection = true;
            System.out.println("Client connected: " + client.getInetAddress());
            this.out = new PrintWriter(server.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(server.getInputStream()));

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    updateOnlineList();
                }
            }).start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the messageInput text to the Server on "enter" press.
     *
     * @param ae
     */
    @FXML
    public void onEnter(ActionEvent ae) {

        if (this.hasConnection) {
            try {
                String text = this.messageInput.getText();
                if (text.equals("/exit")) {
                    this.endConnection();
                }

                if (text.equals("/reset")) {
                    this.messageOutput.clear();
                    this.out.println("/reset");
                } else {
                    this.out.println(text);
                    String line;
                    String message = "";
                    while (true) {
                        Thread.sleep(500);
                        if (this.in.ready()) {
                            line = this.in.readLine();
                            if (line.equals("")) {
                                break;
                            }
                            message += line + "\n";
                        }
                    }
                    System.out.println("Message from server: " + message);
                    this.messageOutput.setText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.messageInput.setText("");
    }

    /**
     * Ends the connection of the client and puts a string into the text-field
     */
    public void endConnection() {
        try {
            this.hasConnection = false;
            this.out.close();
            this.in.close();
            this.server.close();
            this.server = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.messageOutput.appendText("Connection closed. Please restart your connection.");
    }

    private void updateOnlineList() {
        try {
            //TODO: DELETE PRINT AFTER ERROR FIXED
            System.out.println("Listening to Online Clients");
            System.out.println(this.in.ready());
            if (this.in.ready()) {
                String serverMessage = this.in.readLine();
                System.out.println(serverMessage);
                if (serverMessage.startsWith("&/")) {
                    String clientName = serverMessage.substring(2);
                    if (!this.onlineClients.getItems().contains(clientName)) {
                        this.onlineClients.getItems().add(clientName);
                    }
                } else if (serverMessage.startsWith("/&")) {
                    String clientName = serverMessage.substring(2);
                    if (this.onlineClients.getItems().contains(clientName)) {
                        this.onlineClients.getItems().remove(clientName);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}