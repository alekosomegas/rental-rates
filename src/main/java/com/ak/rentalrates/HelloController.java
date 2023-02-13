package com.ak.rentalrates;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;
    ArrayList<WebScrapper> webScrappers = new ArrayList<>();

    @FXML
    protected void onHelloButtonClick() {
        webScrappers.add(new WSPetsas());
        webScrappers.add(new WSHertz());

        for (WebScrapper webScrapper : webScrappers) {
            webScrapper.getQuote();
        }

        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void close() {
        for (WebScrapper webScrapper : webScrappers) {
            webScrapper.close();
        }
    }
}