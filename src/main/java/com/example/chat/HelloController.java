package com.example.chat;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Button sendButton;

    @FXML
    private TextArea taChatHistory;

    @FXML
    private TextField tfMessage;

    private List<String> messageList = new ArrayList<>();


    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
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