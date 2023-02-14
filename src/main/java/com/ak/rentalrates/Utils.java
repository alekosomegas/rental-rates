package com.ak.rentalrates;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Utils {

    public CATEGORY findCategory(String company) {
        switch (company) {
            case "Hertz":
                return CATEGORY.Standard;
            case "Petsas":
                return CATEGORY.Standard;
            default:
                return CATEGORY.Standard;
        }
    }

    public static int[] findPrices(HashMap<String, HashMap<CATEGORY,Integer>> allquotes, CATEGORY category) {
        int[] prices = new int[3];
        ArrayList<Integer> quotes = new ArrayList<>();
        for (HashMap<CATEGORY, Integer> site : allquotes.values()) {
            quotes.add(site.get(category));
        }

        prices[0] = Collections.min(quotes);
        prices[1] = (int) quotes.stream().mapToInt(val -> val).average().orElse(0.0);
        prices[2] = Collections.max(quotes);
        return prices;
    }
}
