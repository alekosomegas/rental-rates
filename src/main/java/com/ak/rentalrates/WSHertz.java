package com.ak.rentalrates;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WSHertz extends WebScrapper{
    @Override
    public void getQuote() {
        driver.get("https://www.hertz.com.cy/");

        WebElement cookies = new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"onetrust-reject-all-handler\"]")));
        cookies.click();

        WebElement location = driver.findElement(By.xpath("//*[@id=\"select2-PickupLocationCode-container\"]/span"));
        location.click();

        new Actions(driver)
                .sendKeys("lim")
                .pause(Duration.ofSeconds(1))
                .sendKeys(Keys.RETURN)
                .perform();

        WebElement pickupDate = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("PickupDate")));
        pickupDate.click();

        int x = 4;
        int y = 3;

        WebElement day = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr["+y+"]/td["+x+"]/a"));
        day.click();

        WebElement returnDate = driver.findElement(By.id("ReturnDate"));
        returnDate.click();
        int x2 = 5;
        int y2 = 3;
        WebElement endDay = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr["+y2+"]/td["+x2+"]/a"));
        endDay.click();

        WebElement submit = driver.findElement(By.xpath("//*[@id=\"booking-step1-form\"]/div[2]/div[1]/div/div[3]/div/button[2]"));
        submit.click();

        WebElement showAll = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"chooseVehicle\"]/div[2]/div[1]/div[2]/div[2]/div/div[2]/div/button")));
        showAll.click();

        List<WebElement> cars = driver.findElements(By.className("e-typo-group"));

        List<WebElement> prices = driver.findElements(By.className("b-availability__price"));

        int count = 0;
        for (int i=0; i < cars.size(); i++) {
            System.out.println(cars.get(i).getText());
            if(i%2==0) {
                System.out.println(prices.get(count).getText());
                count++;
            }

        }
    }

}
