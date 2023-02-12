package com.ak.rentalrates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        WebScrapper webScrapper = new WebScrapper();

        welcomeText.setText("Welcome to JavaFX Application!");
    }
}