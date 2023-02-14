package com.ak.rentalrates;

public class Car {
    CATEGORY category;
    String name;

    public Car(CATEGORY category, String name) {
        this.category = category;
        this.name = name;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }
}
