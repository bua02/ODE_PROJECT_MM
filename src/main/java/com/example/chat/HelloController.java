package com.example.chat;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.net.Socket;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private TextField messageInput;
    private Label welcomeText;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea taChatHistory;

    @FXML
    private TextField tfMessage;

    private List<String> messageList = new ArrayList<>();


    @FXML
    private TextArea messageOutput;
    private Socket client = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private boolean hasConnection = false;

    public HelloController() {
        try{
            Socket client = new Socket("localhost", 4711);
            this.client = client;
            this.hasConnection = true;
            System.out.println("Client connected: " + client.getInetAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the messageInput text to the Server on "enter" press.
     * @param ae
     */
    @FXML
    public void onEnter(ActionEvent ae){
        if(this.hasConnection){
            try {
                this.out = new PrintWriter(client.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String text = this.messageInput.getText();
                if(text.equals("/exit")){
                    this.endConnection();
                }
                this.out.println(text);
                String line = "";
                String message = "";
                //while((line = this.in.readLine()) != null) didnt work - i dont know why..
                while(true){
                    line = this.in.readLine();
                    if(line.equals("")){
                        break;
                    }
                    message += line + "\n";
                }
                System.out.println("Message from server: "+message);
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
        try{
            this.hasConnection = false;
            this.out.close();
            this.in.close();
            this.client.close();
            this.client = null;
        }catch(IOException e){
            e.printStackTrace();
        }
        this.messageOutput.appendText("Connection closed. Please restart your connection.");
    }

    @FXML
    public void onSendButtonPress(ActionEvent e){
        String message = tfMessage.getText();
        messageList.add(message);
        taChatHistory.clear();
        for (String currentMessage:messageList) {
            taChatHistory.appendText(currentMessage.toString() + "\n");
        }
    }
}