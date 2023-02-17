package com.ak.rentalrates.WebSites;


import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.WebClient;
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
    public void findQuotes() {
        try {
            final WebClient webClient = new WebClient();
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            HtmlPage page = webClient.getPage("https://www.leos.com.cy/en/reservation");

            final HtmlInput from = page.getFirstByXPath(XPATHS.get("from"));
            final HtmlInput to = page.getFirstByXPath(XPATHS.get("to"));
            final HtmlButton submit = page.getFirstByXPath(XPATHS.get("submit"));

            // TODO: add calendar
            from.setValue("22/02/2023");
            to.setValue("25/02/2023");
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
        }
    }

    protected int formatPrice(String price) {
        price = price.replace("Total ", "")
                     .replace("EUR", "")
                     .replace(".", "")
                     .strip()
                     .split(",")[0];
        return Integer.parseInt(price);
    }

    // TODO: implement
    @Override
    protected CATEGORY findCategory(String group) {
        return CATEGORY.Luxury_4x4;
    }



}

