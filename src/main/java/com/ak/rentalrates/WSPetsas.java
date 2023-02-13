package com.ak.rentalrates;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WSPetsas extends WebScrapper {

    @Override
    public void getQuote() {
        driver.get("https://www.petsas.com.cy/en/choose-vehicle/bms-2/");

        WebElement clickable = driver.findElement(By.id("puploc"));
        clickable.click();

        WebElement limassol= new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("locOpt_2")));
        limassol.click();

        WebElement date = driver.findElement(By.id("pupdate"));
        date.click();

        int x = 4;
        int y = 3;

        WebElement day = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr["+y+"]/td["+x+"]/a"));
        day.click();

        WebElement endDate = driver.findElement(By.id("dofdate"));
        endDate.click();

        int x2 = 3;
        int y2 = 5;

        WebElement endDay = driver.findElement(By.xpath("//*[@id=\"ui-datepicker-div\"]/table/tbody/tr["+y2+"]/td["+x2+"]/a"));
        endDay.click();

        WebElement submit = driver.findElement(By.xpath("//*[@id=\"searchForm\"]/div[2]/div[3]/button"));
        submit.click();

        WebElement carListContainer = new WebDriverWait(driver, Duration.ofSeconds(1))
                .until(ExpectedConditions.elementToBeClickable(By.id("grp_list")));

        List<WebElement> cars = carListContainer.findElements(By.className("list_item"));

        int count = 0;
        for (WebElement item : cars) {
            System.out.println(count);
            WebElement car = item.findElement(By.tagName("h2"));
            System.out.println(car.getText());
            WebElement td = item.findElement(By.xpath("//*/div[2]/div[2]/div/div[2]/table/tbody/tr[1]/td[2]"));
            System.out.println(td.getText());
            count++;
        }
        driver.close();

    }

}

