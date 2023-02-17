//package com.ak.rentalrates;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicInteger;
//
///*
// *
//
//
// */
//public class WSHertz extends WebScrapper {
//
//    public WSHertz(int[] dateRangeCoords) {
//        driver.get("https://www.hertz.com.cy/");
//        this.dateRangeCoords = dateRangeCoords;
//    }
//    @Override
//    public void findQuotes() {
//
//        int attempts = 5;
//        while(attempts > 0) {
//            WebElement cookies = new WebDriverWait(driver, Duration.ofSeconds(10))
//                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"onetrust-close-btn-container\"]/button")));
//            try {
//                cookies.click();
//                attempts = 0;
//
//            } catch (Exception e) {
//                attempts--;
//            }
//        }
//
//        WebElement location = driver.findElement(By.xpath("//*[@id=\"select2-PickupLocationCode-container\"]/span"));
//        location.click();
//
//        new Actions(driver)
//                .sendKeys("lim")
//                .pause(Duration.ofSeconds(1))
//                .sendKeys(Keys.RETURN)
//                .perform();
//
//        WebElement pickupDate = new WebDriverWait(driver, Duration.ofSeconds(1))
//                .until(ExpectedConditions.elementToBeClickable(By.id("PickupDate")));
//        pickupDate.click();
//
//        int x = dateRangeCoords[0];
//        int y = dateRangeCoords[1];
//
//        WebElement day = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr[" + y + "]/td[" + x + "]/a"));
//        day.click();
//
//        WebElement returnDate = driver.findElement(By.id("ReturnDate"));
//        returnDate.click();
//        int x2 = dateRangeCoords[2];
//        int y2 = dateRangeCoords[3];
//        WebElement endDay = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/div[1]/table/tbody/tr[" + y2 + "]/td[" + x2 + "]/a"));
//        endDay.click();
//
//        WebElement submit = driver.findElement(By.xpath("//*[@id=\"booking-step1-form\"]/div[2]/div[1]/div/div[3]/div/button[2]"));
//        submit.click();
//
//
//        WebElement showAll = new WebDriverWait(driver, Duration.ofSeconds(5))
//                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"chooseVehicle\"]/div[2]/div[1]/div[2]/div[2]/div/div[2]/div/button")));
//        showAll.click();
//
//        List<WebElement> cars = driver.findElements(By.className("e-typo-group"));
//        List<WebElement> carNames = driver.findElements(By.className("e-typo-model"));
//        List<WebElement> prices = driver.findElements(By.className("b-availability__price"));
//
//        int count = 0;
//        String hertzCategory;
//        int price;
//        for (int i = 0; i < cars.size(); i++) {
//            if (i % 2 == 0) {
//                hertzCategory = cars.get(i).getText().substring(6,7);
//                price = Integer.parseInt(prices.get(count).getText().substring(1).split(",")[0].replace(",",""));
//                count++;
//
//                siteQuotes.put(hertzCategory, price);
//            }
//        }
//
//        AtomicInteger countCars = new AtomicInteger();
//        result.append("----------HERTZ----------\n");
//        siteQuotes.entrySet().forEach(entry -> {
//            System.out.println(carNames.get(countCars.get()).getText() + " " + entry.getKey() + "\n" + entry.getValue());
//            result.append(carNames.get(countCars.get()).getText()).append(" ").append(entry.getKey()).append("\n").append(entry.getValue()).append("\n");
//            countCars.getAndIncrement();
//            countCars.getAndIncrement();
//        });
//        driver.close();
//        buildQuotes();
//    }
//
//    public void buildQuotes() {
//        int economic =
//                (int)((siteQuotes.get("A") + siteQuotes.get("B") + siteQuotes.get("G")) / 3);
//        int luxA = siteQuotes.get("M");
//        int luxB = siteQuotes.get("W");
//        int lux4 = siteQuotes.get("Z");
//        int standard =
//                (int)((siteQuotes.get("P")+ siteQuotes.get("H")+ siteQuotes.get("D")) /3);
//        int mpv = (int) (siteQuotes.get("X") * 0.6);
//
//
//        quotes.put(CATEGORY.Economic, economic);
//        quotes.put(CATEGORY.Luxury_A, luxA);
//        quotes.put(CATEGORY.Luxury_B, luxB);
//        quotes.put(CATEGORY.Luxury_4x4, lux4);
//        quotes.put(CATEGORY.Standard, standard);
//        quotes.put(CATEGORY.MPV, mpv);
//    }
//}
