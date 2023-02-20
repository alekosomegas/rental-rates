package com.ak.rentalrates.WebSites;

import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public abstract class WebScrapper implements Runnable{
    final WebClient webClient = new WebClient();
    // stores all the result per each car
    public ArrayList<CarQuote> sitesListOfCarQuotes = new ArrayList<>();
    protected LocalDate fromDate;
    protected LocalDate toDate;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    protected final int waitTime = 10000;
    protected HashMap<String,String> XPATHS = new HashMap<>();
    protected String name;
    public String getName() {
        return name;
    }

    public abstract boolean findQuotes();
    protected abstract CATEGORY findCategory(String group);
    protected abstract int formatPrice(String price);

    public void run() {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);

        int attempts = 0;
        while (attempts < 5) {
            System.out.println("attempt " + attempts);
            boolean success = findQuotes();
            attempts = success ? 5 : ++attempts;
        }
        this.close();
    }

    public ArrayList<CarQuote> getQuotes() {
        return sitesListOfCarQuotes;
    }

    public void close() {
        webClient.close();
    }

}
