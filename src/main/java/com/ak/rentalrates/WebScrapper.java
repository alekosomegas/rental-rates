package com.ak.rentalrates;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;

public abstract class WebScrapper {

    WebDriver driver = new ChromeDriver();
    HashMap<CATEGORY, Integer> quotes = new HashMap<>() {{
        for(CATEGORY category : CATEGORY.values()) {
            put(category, 0);
        }
    }};

    StringBuilder result = new StringBuilder();

    public abstract void findQuotes();

    public void close() {
        driver.close();
    }

    public HashMap<CATEGORY, Integer> getQuotes() {
        return quotes;
    }

    public String getResult() {
        return String.valueOf(result);
    }
}
