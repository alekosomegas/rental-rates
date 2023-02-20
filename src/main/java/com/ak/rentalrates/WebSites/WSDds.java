package com.ak.rentalrates.WebSites;

import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WSDds extends WebScrapper{
    String url;
    String date_from;
    String date_to;

    public WSDds(LocalDate from, LocalDate to) {
        name = "DDS";
        this.fromDate   = from;
        this.toDate     = to;
//        22%2F02%2F2023
        formatter = DateTimeFormatter.ofPattern("dd%2MM%2yyyy");
        date_from = formatter.format(fromDate);
        date_from = date_from.replace("%2", "%2F");
        date_to = formatter.format(toDate);
        date_to = date_to.replace("%2", "%2F");
        url = "https://www.ddscarrentals.com/component/vikrentcar/?option=com_vikrentcar&task=search&Itemid=116&place=1&pickupdate=" +
                date_from +
                "&pickuph=00&pickupm=00&releasedate=" +
                date_to +
                "&releaseh=00&releasem=00&returnplace=1&categories=all&search=Search";

    }
    @Override
    public boolean findQuotes() {
        System.out.println(url);
        try {
            HtmlPage page = webClient.getPage(url);
            page.initialize();

            webClient.waitForBackgroundJavaScript(waitTime);
            page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            List<HtmlDivision> carItems = page.getByXPath("//div[@class = 'vrc-car-result-rightinner']");
            for (HtmlDivision carItem : carItems) {
                Iterable<DomElement> details = carItem.getDomElementDescendants();

                CarQuote carQuote = new CarQuote();
                for (DomElement detail : details) {
                    // car name
                    if(detail.getAttribute("class").equals("vrc-car-name")) {
                        carQuote.setCar(detail.asNormalizedText());
                        carQuote.setCategory(findCategory(detail.asNormalizedText()));
                        continue;
                    }
                    // car price
                    if(detail.getAttribute("class").equals("vrc_price")) {
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
            case "Manual, Small City Car: Suzuki Splash M/T", "Automatic, Small City Car: Toyota Vitz A/T", "Automatic, Small City Car: Suzuki Swift A/T", "Automatic, Small City Car: Nissan March A/T" -> CATEGORY.Economic;
            case "MERCEDES BENZ C220" -> CATEGORY.Luxury_A;
            case "MERCEDES BENZ C250 AMG" -> CATEGORY.Luxury_B;
            case "Automatic, SUV: Honda HR-V A/T", "Automatic, Jeep 4x4: Daihatsu Terios A/T" -> CATEGORY.Luxury_4x4;
            case "Automatic, Small City Car: Mazda Demio A/T", "Automatic, Family Car: Nissan Note A/T", "Automatic, Family Car: Mazda 2 A/T", "Automatic, Family Car: Nissan Tiida Latio A/T", "Automatic, Family Car: Suzuki SX-4 A/T" -> CATEGORY.Standard;
            case "Automatic, Mini Van: Nissan Serena A/T" -> CATEGORY.MPV;
            default -> CATEGORY.None;
        };
    }

    @Override
    protected int formatPrice(String price) {
        price = price
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
