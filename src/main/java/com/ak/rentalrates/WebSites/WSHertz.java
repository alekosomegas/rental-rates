package com.ak.rentalrates.WebSites;

import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.html.*;

import java.time.LocalDate;
import java.util.List;

public class WSHertz extends WebScrapper {

    public WSHertz(LocalDate from, LocalDate to) {
        name = "Hertz";
        this.fromDate   = from;
        this.toDate     = to;

        XPATHS.put("location", "//*[@id=\"select2-PickupLocationCode-container\"]/span");
        XPATHS.put("limassol", "/html/body/span/span/span[1]/input");
        XPATHS.put("limOption", "//*[@id=\"select2-PickupLocationCode-results\"]/li");
        XPATHS.put("from", "//*[@id=\"PickupDate\"]");
        XPATHS.put("to", "//*[@id=\"ReturnDate\"]");
        XPATHS.put("submit", "//*[@id=\"booking-step1-form\"]/div[2]/div[1]/div/div[3]/div/button[2]");
        XPATHS.put("showAll", "//*[@id=\"chooseVehicle\"]/div[2]/div[1]/div[2]/div[2]/div/div[2]/div/button");
    }
    @Override
    public boolean findQuotes() {
        try {
            HtmlPage page = webClient.getPage("https://www.hertz.com.cy/en");
            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlSpan location = page.getFirstByXPath(XPATHS.get("location"));
            page = location.click();
            HtmlInput limassol = page.getFirstByXPath(XPATHS.get("limassol"));
            limassol.type("limassol");

            webClient.waitForBackgroundJavaScript(waitTime);
            HtmlListItem limOption = page.getFirstByXPath(XPATHS.get("limOption"));
            page = limOption.click();

            HtmlInput from      = page.getFirstByXPath(XPATHS.get("from"));
            HtmlInput to        = page.getFirstByXPath(XPATHS.get("to"));
            HtmlButton submit   = page.getFirstByXPath(XPATHS.get("submit"));

            from.setValue(formatter.format(fromDate));
            to.setValue(formatter.format(toDate));
            webClient.waitForBackgroundJavaScript(waitTime);
            submit.click();

            webClient.waitForBackgroundJavaScript(waitTime);
            page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            HtmlButton showAll = page.getFirstByXPath(XPATHS.get("showAll"));
            showAll.click();

            webClient.waitForBackgroundJavaScript(waitTime);
            page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            List<HtmlDivision> carItems = page.getByXPath("//div[@class = 's-chooseVehicle__vehicle']");
//            //   var json_vehicles = {  "CarsView": get this and find keys
//            System.out.println(page.asXml());
            for (HtmlElement carItem : carItems) {
//                System.out.println(carItem.asXml());
                Iterable<DomElement> details = carItem.getDomElementDescendants();
                CarQuote carQuote = new CarQuote();
                for (DomElement detail : details) {
                    // car name
                    if(detail.getAttribute("class").equals("e-typo-model")) {
                        carQuote.setCar(detail.asNormalizedText());
                        continue;
                    }
                    // car group
                    if(detail.getAttribute("class").equals("e-typo-group")) {
                        carQuote.setGroup(detail.asNormalizedText());
                        carQuote.setCategory(findCategory(detail.asNormalizedText()));
                        continue;
                    }
                    // car price
                    if(detail.getAttribute("class").equals("b-availability__price")) {
                        String price = detail.asNormalizedText();
                        carQuote.setPrice(formatPrice(price));
                        break;
                    }
                }
                this.sitesListOfCarQuotes.add(carQuote);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected CATEGORY findCategory(String group) {
        try {
            group = group.split("\\|")[0]
                    .substring(6)
                    .strip();
        } catch (StringIndexOutOfBoundsException e) {
            return CATEGORY.None;
        }
        return switch (group) {
            case "A", "B", "G" -> CATEGORY.Economic;
            case "M" -> CATEGORY.Luxury_A;
            case "W" -> CATEGORY.Luxury_B;
            case "Z" -> CATEGORY.Luxury_4x4;
            case "P", "H" -> CATEGORY.Standard;
            case "X" -> CATEGORY.MPV;
            default -> CATEGORY.None;
        };
    }

    @Override
    protected int formatPrice(String price) {
        price = price.replace("€", "")
                .replace(",", "")
                .strip()
                .split("\\.")[0];
        try {
            return Integer.parseInt(price);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
