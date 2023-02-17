package com.ak.rentalrates.WebSites;

import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class WebScrapper implements Runnable{
    // stores all the result per each car
    public ArrayList<CarQuote> sitesListOfCarQuotes = new ArrayList<>();
    protected LocalDate fromDate;
    protected LocalDate toDate;
    protected final int waitTime = 10000;
    protected HashMap<String,String> XPATHS = new HashMap<>();
    protected String name;
    public String getName() {
        return name;
    }

    public abstract void findQuotes();
    protected abstract CATEGORY findCategory(String group);
    protected abstract int formatPrice(String price);

    public void run() {
        findQuotes();
    }

    public ArrayList<CarQuote> getQuotes() {
        return sitesListOfCarQuotes;
    }


    public void close() {

    }

}
