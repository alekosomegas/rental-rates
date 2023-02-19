package com.ak.rentalrates.WebSites;

import com.ak.rentalrates.CATEGORY;
import com.ak.rentalrates.CarQuote;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class WSPetsas extends WebScrapper {
    public WSPetsas(LocalDate from, LocalDate to) {
        name = "Petsas";

        // TODO: send message to be displayed, need 2 days in advance
//        if (Duration.between(from.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() < 3) {
//            return ;
//        }
        this.fromDate = from;
        this.toDate = to;

        XPATHS.put("location", "//*[@id=\"puploc\"]");
        XPATHS.put("limassol", "//*[@id=\"locOpt_2\"]");
        XPATHS.put("from", "//*[@id=\"pupdate\"]");
        XPATHS.put("tableBody", "//*[@id=\"ui-datepicker-div\"]/table/tbody");
        XPATHS.put("to", "//*[@id=\"dofdate\"]");
        XPATHS.put("submit", "//*[@id=\"searchForm\"]/div[2]/div[3]/button");
        XPATHS.put("cars", "//*[@id=\"grp_list\"]");
        XPATHS.put("month", "//*[@id=\"ui-datepicker-div\"]/div/div/select[1]");
        XPATHS.put("year", "//*[@id=\"ui-datepicker-div\"]/div/div/select[2]");
    }

    @Override
    public void findQuotes() {
        try {
            final WebClient webClient = new WebClient();
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);

            HtmlPage page = webClient.getPage("https://www.petsas.com.cy/en/choose-vehicle/bms-2/");
            webClient.waitForBackgroundJavaScript(waitTime);

            HtmlInput location = page.getFirstByXPath(XPATHS.get("location"));
            page = location.click();

            HtmlInput limassol = page.getFirstByXPath(XPATHS.get("limassol"));
            page = limassol.click();

            HtmlInput from = page.getFirstByXPath(XPATHS.get("from"));
            page = from.click();

            HtmlSelect month = page.getFirstByXPath(XPATHS.get("month"));
            List<HtmlOption> listOfMonths = month.getOptions();
            for (HtmlOption option : listOfMonths) {
                if (option.getText().equals(fromDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH))) {
                    option.setSelected(true);
                    break;
                }
            }

            HtmlTableBody tableBody = page.getFirstByXPath(XPATHS.get("tableBody"));
            // get all anchor elements in the fromBody
            List<HtmlElement> listOfDates = tableBody.getElementsByAttribute("a", "href", "#");

            for (HtmlElement date : listOfDates) {
                if(date.asNormalizedText().equals(String.valueOf(this.fromDate.getDayOfMonth()))) {
                    page = date.click();
                    break;
                }
            }

            HtmlInput to = page.getFirstByXPath(XPATHS.get("to"));
            page = to.click();

            HtmlSelect monthTo = page.getFirstByXPath(XPATHS.get("month"));
            List<HtmlOption> listOfMonthsTo = monthTo.getOptions();
            for (HtmlOption option : listOfMonthsTo) {
                if (option.getText().equals(toDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH))) {
                    page = (HtmlPage) option.setSelected(true);
                    monthTo.setSelectedAttribute(option, true);
                    break;
                }
            }

            tableBody = page.getFirstByXPath(XPATHS.get("tableBody"));
            listOfDates = tableBody.getElementsByAttribute("a", "href", "#");

            for (HtmlElement date : listOfDates) {
                if(date.asNormalizedText().equals(String.valueOf(this.toDate.getDayOfMonth()))) {
                    page = date.click();
                    break;
                }
            }

            HtmlButton submit = page.getFirstByXPath(XPATHS.get("submit"));
            submit.click();

            webClient.waitForBackgroundJavaScript(waitTime);
            page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();

            HtmlDivision cars = page.getFirstByXPath(XPATHS.get("cars"));
            Iterable<DomNode> listOfItems = cars.getChildren();

            Iterable<DomElement> itemDetails;
            for (DomNode item : listOfItems) {
                itemDetails = item.getDomElementDescendants();
                CarQuote carQuote = new CarQuote();
                for (DomElement detail : itemDetails) {
                    // car name
                    if(detail.getNodeName().equals("h2")) {
                        carQuote.setCar(detail.asNormalizedText());
                        continue;
                    }
                    // car group
                    if(detail.getNodeName().equals("strong")) {
                        carQuote.setGroup(detail.asNormalizedText());
                        carQuote.setCategory(findCategory(detail.asNormalizedText()));
                        continue;
                    }
                    // car price
                    if(detail.getNodeName().equals("tr")) {
                        carQuote.setPrice(formatPrice(detail.getLastElementChild().asNormalizedText()));
                        this.sitesListOfCarQuotes.add(carQuote);
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    protected int formatPrice(String price) {
        price = price.replace("€", "")
                .replace(",","")
                .split("\\.")[0];
        try {
            return Integer.parseInt(price);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    protected CATEGORY findCategory(String group) {
        group = group.split(" - ")[0].substring(7);
        return switch (group) {
            case "A3", "B3", "A5", "C2", "B5", "D5" -> CATEGORY.Economic;
            case "E5", "H2" -> CATEGORY.Luxury_A;
            case "H6", "H7" -> CATEGORY.Luxury_B;
            case "G9" -> CATEGORY.Luxury_4x4;
            case "C4", "EC" -> CATEGORY.Standard;
            case "V3" -> CATEGORY.MPV;
            default -> null;
        };
    }
}

