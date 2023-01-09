package com.example.chat;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        for (String currentMessage:messageList) {
            messageList.add(currentMessage.toString() + "\n");
        }
    }
}