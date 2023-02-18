package com.ak.rentalrates;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RentalRatesApplication extends Application {

    private RentalRatesController controller;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RentalRatesApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        controller = fxmlLoader.getController();
    }

    @Override
    public void stop(){
        System.out.println("Stage is closing");
        controller.close();
    }

    public static void main(String[] args) {


        launch();
    }
}