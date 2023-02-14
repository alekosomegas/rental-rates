package com.ak.rentalrates;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RentalRatesController {
    @FXML
    public DatePicker fromDatePicker;
    @FXML
    public DatePicker toDatePicker;
    @FXML
    public Label lowPrice;
    @FXML
    public Label averagePrice;
    @FXML
    public Label highPrice;
    @FXML
    public ChoiceBox<String> selectCar;
    public TextArea taResults;

    private Car selectedCar;
    private ObservableList<Car> allCars = FXCollections.observableArrayList();
    private ArrayList<WebScrapper> webScrappers = new ArrayList<>();
    // Holds all the quotes from each site organised by category
    private HashMap<String, HashMap<CATEGORY,Integer>> allQuotes = new HashMap<>();

    private int[] dateRangeCoords = new int[4];
    @FXML
    public void initialize() {
        fromDatePicker.valueProperty().addListener((ov,oldValue,newValue) -> {
            dateRangeCoords[0] = fromDatePicker.getValue().getDayOfWeek().getValue() ;
            dateRangeCoords[1] = Math.floorDiv(fromDatePicker.getValue().getDayOfMonth(), 7) +1;

            for (Integer i : dateRangeCoords
                 ) {
            System.out.println(i);
            }
        });
        toDatePicker.valueProperty().addListener((ov,oldValue,newValue) -> {
            dateRangeCoords[2] = toDatePicker.getValue().getDayOfWeek().getValue() ;
            dateRangeCoords[3] = Math.floorDiv(toDatePicker.getValue().getDayOfMonth(), 7) +1 ;

            for (Integer i : dateRangeCoords
            ) {
                System.out.println(i);
            }
        });

        try {
            allCars = Cars.getAllCars();
            ObservableList<String> list = FXCollections.observableArrayList();
            for(Car car : allCars) {
                list.add(car.getName());
            }
            selectCar.setItems(list);
            selectCar.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    selectedCar = Cars.findCar(t1);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onHelloButtonClick() throws InterruptedException {
        webScrappers.clear();
        webScrappers.add(new WSPetsas(dateRangeCoords));
        webScrappers.add(new WSHertz(dateRangeCoords));

        Thread t;
        ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (WebScrapper webScrapper : webScrappers) {
//            t = new Thread((Runnable) webScrapper);
//            t.start();
            executor.execute((Runnable) webScrapper);
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);

        for (WebScrapper webScrapper : webScrappers) {
            String site = webScrapper.getClass().getName().replace("com.ak.rentalrates.WS", "");
//            webScrapper.findQuotes();
            allQuotes.put(site, webScrapper.getQuotes());
            taResults.setText(taResults.getText() + "\n\n" + webScrapper.getResult());
        }
        int[] prices = Utils.findPrices(allQuotes, selectedCar.getCategory());
        lowPrice.setText(String.valueOf(prices[0]));
        averagePrice.setText(String.valueOf(prices[1]));
        highPrice.setText(String.valueOf(prices[2]));

    }

    public void close() {
        for (WebScrapper webScrapper : webScrappers) {
            webScrapper.close();
        }
    }
}