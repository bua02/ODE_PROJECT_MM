package com.example.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;

public class ClientController {
    public static final String LOCALHOST = "localhost";
    public static final int PORT = 4711;
    @FXML
    private TextField messageInput;
    @FXML
    private TextArea messageOutput;
    private Socket client;
    private PrintWriter out;
    private BufferedReader in;
    private boolean hasConnection = false;

    public ClientController() {
        try {
            Socket client = new Socket(LOCALHOST, PORT);
            this.client = client;
            this.hasConnection = true;
            System.out.println("Client connected: " + client.getInetAddress());
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
                this.out = new PrintWriter(client.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String text = this.messageInput.getText();
                if (text.equals("/exit")) {
                    this.endConnection();
                }
                this.out.println(text);
                String line;
                String message = "";

                while (true) {
                    line = this.in.readLine();
                    if (line.equals("")) {
                        break;
                    }
                    message += line + "\n";
                }
                System.out.println("Message from server: " + message);
                this.messageOutput.setText(message);
            } catch (IOException e) {
                e.printStackTrace();
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
            this.client.close();
            this.client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.messageOutput.appendText("Connection closed. Please restart your connection.");
    }
}