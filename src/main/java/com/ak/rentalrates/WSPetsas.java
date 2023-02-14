package com.ak.rentalrates;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;

public class WSPetsas extends WebScrapper implements Runnable {

    int[] dateRangeCoords;

    HashMap<String, Integer> petsasQuotes = new HashMap<>();

    @Override
    public void run() {
        findQuotes();
    }
    public WSPetsas(int[] dateRangeCoords) {
        this.dateRangeCoords = dateRangeCoords;
        driver.get("https://www.petsas.com.cy/en/choose-vehicle/bms-2/");
    }

    @Override
    public void findQuotes() {

        WebElement clickable = driver.findElement(By.id("puploc"));
        clickable.click();

        WebElement limassol = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("locOpt_2")));
        limassol.click();

        WebElement date = driver.findElement(By.id("pupdate"));
        date.click();

        int x = adjustX(dateRangeCoords[0]);
        int y = dateRangeCoords[1];

        WebElement day = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[" + y + "]/td[" + x + "]/a"));
        day.click();

        WebElement endDate = driver.findElement(By.id("dofdate"));
        endDate.click();

        int x2 = adjustX(dateRangeCoords[2]);
        int y2 = dateRangeCoords[3];

        WebElement endDay = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr[" + y2 + "]/td[" + x2 + "]/a"));
        endDay.click();

        WebElement submit = driver.findElement(By.xpath("//*[@id=\"searchForm\"]/div[2]/div[3]/button"));
        submit.click();

        WebElement carListContainer = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("grp_list")));

        List<WebElement> cars = carListContainer.findElements(By.className("list_item"));

        String petsasCategory;
        int price;
        result.append("----------PETSAS----------\n");
        for (WebElement item : cars) {
            WebElement car = item.findElement(By.tagName("strong"));
            petsasCategory = car.getText().split(" - ")[0].substring(7);
            System.out.println(item.findElement(By.tagName("h2")).getText());
            result.append(item.findElement(By.tagName("h2")).getText() + "\n");
            WebElement td = item.findElements(By.tagName("td")).get(1);
            price = Integer.parseInt(
                    td.getText().substring(1).split("\\.")[0].replace(",","")
            );
            System.out.print(" " + price + "\n");
            result.append(" " + price + "\n");
            petsasQuotes.put(petsasCategory, price);
        }

        driver.close();
        buildQuotes();
    }

    public void buildQuotes() {
        int economic =
                (int)((petsasQuotes.get("A3") + petsasQuotes.get("B3") + petsasQuotes.get("A5")
                        +petsasQuotes.get("C2") + petsasQuotes.get("B5") + petsasQuotes.get("D5")
                ) / 6);
        int luxA = (int)((petsasQuotes.get("E5") + petsasQuotes.get("H2")) /2);
        int luxB = (int)((petsasQuotes.get("H6") + petsasQuotes.get("H7")) /2);
        int lux4 = petsasQuotes.get("G9");
        int standard =
                (int)((petsasQuotes.get("C4")+petsasQuotes.get("EC")) /2);
        int mpv = (int) (petsasQuotes.get("V3") * 0.6);


        quotes.put(CATEGORY.Economic, economic);
        quotes.put(CATEGORY.Luxury_A, luxA);
        quotes.put(CATEGORY.Luxury_B, luxB);
        quotes.put(CATEGORY.Luxury_4x4, lux4);
        quotes.put(CATEGORY.Standard, standard);
        quotes.put(CATEGORY.MPV, mpv);
    }

    public int adjustX(int x) {
        if (x==7) return 1;
        return x+1;
    }
}

