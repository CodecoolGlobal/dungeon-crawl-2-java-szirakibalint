package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.items.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Inventory {
    private List<Item> inventory;

    public Inventory() {
        inventory = new ArrayList<>();
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public void add(Item item) {
        inventory.add(item);
    }

    public boolean hasKey() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getTileName().equals("key")) {
                return true;
            }
        }
        return false;
    }

    public boolean useKey() {
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).getTileName().equals("key")) {
                inventory.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Item> getContent(){
        return inventory;
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
