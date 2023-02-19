//package com.ak.rentalrates;
//
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.Duration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
////TODO: use selenium
//public class WSEuropcar extends WebScrapper {
//    private final int waitTime = 10;
//    private final Map<String, String> XPATHS = Map.of(
//       "location",
//            "/html/body/section/div[5]/div[2]/div/section[1]/ul/li[1]/a/div[2]/span",
//        "submit",
//            "//*[@id=\"search-btn\"]",
//        "from",
//            "//*[@id=\"datepicker-pickup\"]",
//        "to",
//            "//*[@id=\"datepicker-return\"]",
//        "month",
//            "//*[@id=\"ui-datepicker-div\"]/div/div/span[1]",
//        "calendarBody",
//            "//*[@id=\"ui-datepicker-div\"]/table/tbody",
//        "next",
//            "//*[@id=\"ui-datepicker-div\"]/div/a[2]",
//        "prev",
//            "//*[@id=\"ui-datepicker-div\"]/div/a[1]"
//
//    );
//
//    public WSEuropcar() {
//        driver.get("https://www.europcar.com/en/car-rental/locations/cyprus/limassol");
//    }
//    @Override
//    public void findQuotes() {
//        try {
//            WebElement location = new WebDriverWait(driver, Duration.ofSeconds(waitTime))
//                    .until(ExpectedConditions.elementToBeClickable(By.xpath(XPATHS.get("location"))));
//            location.click();
//
//
//            WebElement from = new WebDriverWait(driver, Duration.ofSeconds(waitTime))
//                    .until(ExpectedConditions.elementToBeClickable(By.xpath("from")));
//            from.click();
////            WebElement month = new WebDriverWait(driver, Duration.ofSeconds(waitTime))
////                    .until(ExpectedConditions.elementToBeClickable(By.xpath("month")));
////            System.out.println(month.getText());
////            WebElement calendarBody = driver.findElement(By.xpath("calendarBody"));
////            List<WebElement> daysList = calendarBody.findElements(By.tagName("td"));
////
////            for (WebElement w :
////                    daysList) {
////                System.out.println(w.getText());
////            }
//
////            WebElement submit = new WebDriverWait(driver, Duration.ofSeconds(waitTime))
////                    .until(ExpectedConditions.elementToBeClickable(By.xpath(XPATHS.get("submit"))));
////            submit.click();
//        } catch (Exception e) {
//
//        }
//    }
//}
//
