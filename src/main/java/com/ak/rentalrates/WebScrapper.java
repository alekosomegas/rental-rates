package com.ak.rentalrates;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public abstract class WebScrapper {

    WebDriver driver = new ChromeDriver();

    public abstract void getQuote();

    public void close() {
        driver.close();
    }
}
