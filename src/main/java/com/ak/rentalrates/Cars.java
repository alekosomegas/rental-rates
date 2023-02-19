package com.ak.rentalrates;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;

public class Cars {
    private static final ObservableList<Car> allCars = FXCollections.observableArrayList();

    public static ObservableList<Car> getAllCars() throws IOException {
        File file = new File("./cars.txt");

        BufferedReader br
                = new BufferedReader(new FileReader(file));

        String line;
        String name;
        CATEGORY category;
        while ((line = br.readLine()) != null) {
            name = line.split(",")[0];
            category = CATEGORY.valueOf(line.split(",")[1]);
            allCars.add(new Car(category, name));
        }
        return allCars;
    }

    public static Car findCar(String name) {
        for (Car car : allCars) {
            if (car.getName().equals(name)) {
                return car;
            }
        }
        // return a null car to search all quotes
        return new Car(CATEGORY.None, "null");
    }
}
