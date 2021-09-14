package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.items.Item;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Inventory {
    ArrayList<Item> inventory;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    public void add(Item item) {
        inventory.add(item);
    }

    @Override
    public String toString() {
        if (inventory.size() == 0) {
            return "Inventory is empty";
        } else {
            StringBuilder sb = new StringBuilder("Inventory:\n");
            StringJoiner sj = new StringJoiner("\n");
            for (Item item: inventory) {
                sj.add(item.toString());
            }
            sb.append(sj);
            return sb.toString();
        }
    }
}
