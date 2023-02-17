package com.ak.rentalrates;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//TODO: ad functions to return proper types for gui to display, ie. all, per site, per category etc
public class Results {
    // Holds all results for each LocalDate range
    private HashMap<String, HashMap<String, ArrayList<CarQuote>>> results = new HashMap<>();

    public HashMap<String, ArrayList<CarQuote>> getResult(LocalDate from, LocalDate to) {
        return results.get(findRange(from, to));
    }

    public void addResult(LocalDate from ,LocalDate to, String siteName, ArrayList<CarQuote> siteQuotes) {
        if (has(from, to)) {
            getResult(from, to).put(siteName, siteQuotes);
        } else {
            HashMap<String, ArrayList<CarQuote>> siteResults = new HashMap<>();
            siteResults.put(siteName, siteQuotes);
            results.put(findRange(from, to), siteResults);
        }
    }

    public boolean has(LocalDate from, LocalDate to) {
        return results.containsKey(findRange(from, to));
    }

    public String findRange(LocalDate from, LocalDate to) {
        return from.toString() + "-" + to.toString();
    }


    public int[] getPrices(LocalDate from, LocalDate to, CATEGORY category) {
        int[] prices = new int[3];
        ArrayList<Integer> quotesPrices = new ArrayList<>();
            for (ArrayList<CarQuote> sitesQuotes : getResult(from, to).values()) {
                for (CarQuote carQuote : sitesQuotes) {
                    if(carQuote.getCategory() == category) {
                        Integer price = carQuote.getPrice();
                        if(price > 0) quotesPrices.add(price);
                    }
                }
            }

        // TODO: if no category match then quotesPrices will be empty - is that ok?
        prices[0] = Collections.min(quotesPrices);
        prices[1] = (int) quotesPrices.stream().mapToInt(val -> val).average().orElse(0.0);
        prices[2] = Collections.max(quotesPrices);
        return prices;
    }

    public String getQuotesForCategory(LocalDate from, LocalDate to, CATEGORY category) {
        StringBuilder st = new StringBuilder("Quotes for category " + category + "\n\n");
        for (Map.Entry<String, ArrayList<CarQuote>> siteResults : getResult(from, to).entrySet()) {
            st.append("***************************\n");
            st.append(siteResults.getKey()).append("\n");
            st.append("***************************\n");
            for(CarQuote carQuote : siteResults.getValue()) {
                if(carQuote.getCategory() == category) {
                    st.append(carQuote).append("\n");
                    st.append("----------------------------\n");
                }
            }
        }
        return st.toString();
    }
}
