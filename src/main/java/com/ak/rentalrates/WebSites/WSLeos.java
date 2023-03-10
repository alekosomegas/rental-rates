package com.ak.rentalrates.WebSites;


import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.html.*;

import java.time.LocalDate;
import java.util.List;

public class WSLeos extends WebScrapper{

    public WSLeos(LocalDate from, LocalDate to) {
        name = "Leos";
        this.fromDate = from;
        this.toDate = to;

        XPATHS.put("from", "//*[@id=\"datum_preuzimanja\"]");
        XPATHS.put("to", "//*[@id=\"datum_povrata\"]");
        XPATHS.put("submit", "//*[@id=\"form-reservation\"]/div/div[2]/div/div/div[1]/button");
        XPATHS.put("price", "//div[contains(@class, 'price')]");
        XPATHS.put("carItems", "//div[contains(@class, 'carsList-item')]");
    }

    @Override
    public boolean findQuotes() {
        try {
            HtmlPage page = webClient.getPage("https://www.leos.com.cy/en/reservation");
            webClient.waitForBackgroundJavaScript(waitTime);

            final HtmlInput from = page.getFirstByXPath(XPATHS.get("from"));
            final HtmlInput to = page.getFirstByXPath(XPATHS.get("to"));
            final HtmlButton submit = page.getFirstByXPath(XPATHS.get("submit"));

            from.setValue(formatter.format(fromDate));
            to.setValue(formatter.format(toDate));
            submit.click();

            webClient.waitForBackgroundJavaScript(waitTime);
            page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            List<HtmlDivision> carItems = page.getByXPath(XPATHS.get("carItems"));
            for (HtmlElement carItem : carItems) {
                Iterable<DomElement> details = carItem.getDomElementDescendants();
                CarQuote carQuote = new CarQuote();
                for (DomElement detail : details) {
                    // car name
                    if(detail.getNodeName().equals("h2")) {
                        carQuote.setCar(detail.asNormalizedText());
                        continue;
                    }
                    // car group
                    if(detail.getAttribute("class").equals("suptitle c_orange t_upper f_regular")) {
                        carQuote.setGroup(detail.asNormalizedText());
                        carQuote.setCategory(findCategory(detail.asNormalizedText()));
                        continue;
                    }
                    // car price
                    if(detail.getAttribute("class").equals("price")) {
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

    protected int formatPrice(String price) {
        price = price.replace("Total ", "")
                     .replace("EUR", "")
                     .replace(".", "")
                     .strip()
                     .split(",")[0];
        try {
            return Integer.parseInt(price);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    protected CATEGORY findCategory(String group) {
        try {
            group = group.split("/")[0].substring(7, 9);
        } catch (StringIndexOutOfBoundsException e) {
            return CATEGORY.None;
        }
        return switch (group) {
            case "A1", "A2", "B1", "B2" -> CATEGORY.Economic;
            case "F1" -> CATEGORY.Luxury_A;
            case "G1" -> CATEGORY.Luxury_B;
            case "C4", "H2", "F2", "D2" -> CATEGORY.Luxury_4x4;
            case "C1", "C2" -> CATEGORY.Standard;
            case "E1" -> CATEGORY.MPV;
            default -> CATEGORY.None;
        };
    }



}

