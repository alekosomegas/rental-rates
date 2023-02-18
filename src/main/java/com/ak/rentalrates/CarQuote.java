package com.ak.rentalrates;

public class CarQuote {
    private String car = null;
    private String group = null;
    private Integer price = 0;

    private CATEGORY category;

    public void setCar(String car) {
        if (this.car != null) return;
        this.car = car;
    }

    public void setGroup(String group) {
        if (this.group != null) return;
        this.group = group;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (this.price != 0) return;
        this.price = price;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return " -> CAR: " + car + "\n" + " -> PRICE: " + String.format("€%,d",price);
    }
}