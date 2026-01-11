package com.example.pantrypal;

import java.util.ArrayList;
import java.util.List;

public class PantryRepository {

    private static final List<PantryItem> pantryList = new ArrayList<>();

    public static void addItem(PantryItem item) {
        pantryList.add(item);
    }

    public static List<PantryItem> getItems() {
        return pantryList;
    }
}
