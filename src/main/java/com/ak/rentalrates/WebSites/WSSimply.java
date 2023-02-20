package com.ak.rentalrates.WebSites;

import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WSSimply extends WebScrapper{
    String date_from;
    String date_to;

    public WSSimply(LocalDate from, LocalDate to) {
        name = "Simply";
        this.fromDate   = from;
        this.toDate     = to;
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date_from = formatter.format(fromDate);
        date_to = formatter.format(toDate);
    }

    @Override
    public boolean findQuotes() {
        try {
            String url = "https://www.simplyrentalcars.com/reservations.html/cars/date_from:"+date_from+"/time_from:09:00/date_to:"+date_to+"/time_to:09:00/pickup_id:2/same_location:1/type_id:0/sorting:sort%7Casc";

            HtmlPage page = webClient.getPage(url);
            page.initialize();

            webClient.waitForBackgroundJavaScript(waitTime);
            page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            List<HtmlDivision> carItems = page.getByXPath("//div[@class = 'product cms-e-section']");

            for (HtmlDivision carItem : carItems) {
                Iterable<DomElement> details = carItem.getDomElementDescendants();

                CarQuote carQuote = new CarQuote();
                for (DomElement detail : details) {
                    // car name
                    if(detail.getNodeName().equals("h3")) {
                        carQuote.setCar(detail.asNormalizedText());
                        carQuote.setCategory(findCategory(detail.asNormalizedText()));
                        continue;
                    }
                    // car price
                    if(detail.getNodeName().equals("strong")) {
                        String price = detail.asNormalizedText();
                        int iprice = formatPrice(price);
                        if (iprice > 0) {
                            carQuote.setPrice(formatPrice(price));
                            this.sitesListOfCarQuotes.add(carQuote);
                            break;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected CATEGORY findCategory(String group) {
        group = group.strip();
        return switch (group) {
            case "HONDA CIVIC", "TOYOTA VITZ", "SUZUKI SWIFT", "MAZDA DEMIO", "CITROEN C1" , "TOYOTA AYGO" -> CATEGORY.Economic;
            case "MERCEDES BENZ C220", "BMW 320 ESTATE", "BMW 225 D", "MERCEDES A200 AMG" -> CATEGORY.Luxury_A;
            case "MERCEDES BENZ C250 AMG", "MERCEDES BENZ E220 AMG" -> CATEGORY.Luxury_B;
            case "NISSAN X TRAIL", "SUBARU XVI SE PREMIUM", "F2", "D2" -> CATEGORY.Luxury_4x4;
            case "KIA CEED ESTATE", "FORD C-MAX", "MAZDA 3", "FORD FIESTA", "NISSAN NOTE" -> CATEGORY.Standard;
            case "FORD GALAXY" -> CATEGORY.MPV;
            default -> CATEGORY.None;
        };
    }
    @Override
    protected int formatPrice(String price) {
        price = price.replace("€ ", "")
                .replace(",", "")
                .strip()
                .split("\\.")[0];
        try {
            return Integer.parseInt(price);
        } catch (Exception e) {
            return 0;
        }
    }
}
