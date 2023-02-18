package com.ak.rentalrates;

import com.ak.rentalrates.WebSites.WSLeos;
import com.ak.rentalrates.WebSites.WSPetsas;
import com.ak.rentalrates.WebSites.WebScrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
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
    @FXML
    public TextArea taResults;
    @FXML
    public Button getQuotesBtn;
    public Label lowPricePD;
    public Label averagePricePD;
    public Label highPricePD;

    private Car selectedCar;
    private ObservableList<Car> allCars = FXCollections.observableArrayList();

    private ArrayList<WebScrapper> webScrappers = new ArrayList<>();
    private Results results = new Results();

    LocalDate from;
    LocalDate to;

    MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        try {
            Media media = new Media(Objects.requireNonNull(getClass().getResource("/sounds/success.mp3")).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        fromDatePicker.setValue(LocalDate.of(2023, 2, 20));
        toDatePicker.setValue(LocalDate.of(2023, 3, 20));

        loadCars();
    }

    /**
     * Load all cars from cars.txt
     */
    // TODO: what happens when cannot read from cars.txt, when selectedCar is null
    private void loadCars() {
        try {
            allCars = Cars.getAllCars();
            ObservableList<String> list = FXCollections.observableArrayList();
            for(Car car : allCars) {
                list.add(car.getName());
            }
            selectCar.setItems(list);

        } catch (IOException e) {
            e.printStackTrace();
        }

        selectCar.setValue("All");
        selectCar.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                selectedCar = Cars.findCar(t1);
            }
        });
    }

    @FXML
    protected void onGetQuotesButtonClick() throws InterruptedException {
        from = fromDatePicker.getValue();
        to   = toDatePicker.getValue();

        if (!results.has(from, to)) {
            scrapSites();
        } else {
           results.getResult(from, to);
        }
        // update gui
        setPricesToLabels();
        setQuotesForCategory();

        if(mediaPlayer != null) mediaPlayer.setAutoPlay(true);
    }

    private void setPricesToLabels() {
        int[] prices = results.getPrices(from, to, selectedCar.getCategory());
        double daysRented = (double) Duration.between(from.atStartOfDay(), to.atStartOfDay()).toDays();

        String format = "€%,d";
        lowPrice.setText(String.format(format, prices[0]));
        averagePrice.setText(String.format(format, prices[1]));
        highPrice.setText(String.format(format, prices[2]));

        format = "(€%,.0f)";
        lowPricePD.setText(String.format(format, (double) prices[0]/daysRented));
        averagePricePD.setText(String.format(format, (double) prices[1]/daysRented));
        highPricePD.setText(String.format(format, (double) prices[2]/daysRented));
    }

    private void setQuotesForCategory() {
        taResults.setText(results.getQuotesForCategory(from, to, selectedCar.getCategory()));
    }

    private void scrapSites() throws InterruptedException {
        // remove previous webscrappers
        webScrappers.clear();
        // add webscrappers to the list
        webScrappers.add(new WSPetsas(fromDatePicker.getValue(), toDatePicker.getValue()));
        webScrappers.add(new WSLeos(fromDatePicker.getValue(), toDatePicker.getValue()));
//        webScrappers.add(new WSPetsas(dateRangeCoords));
//        webScrappers.add(new WSHertz(dateRangeCoords));
//        webScrappers.add(new WSLeos(fromDatePicker.getValue(), toDatePicker.getValue()));
//        webScrappers.add(new WSEuropcar());

        ExecutorService executor= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (WebScrapper webScrapper : webScrappers) {
            executor.execute(webScrapper);
        }
        executor.shutdown();
        boolean finished = executor.awaitTermination(1, TimeUnit.MINUTES);

        for (WebScrapper webScrapper : webScrappers) {
            results.addResult(from, to, webScrapper.getName(), webScrapper.getQuotes());
        }
    }

    public void close() {
        for (WebScrapper webScrapper : webScrappers) {
            webScrapper.close();
        }
    }
}