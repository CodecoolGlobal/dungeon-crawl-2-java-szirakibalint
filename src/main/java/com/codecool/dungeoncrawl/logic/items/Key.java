package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Key extends Item {

    public Key(Cell cell) {
        super(cell);
        super.setName("Key");
    }

    public Key() {
    }

    @Override
    public String getTileName() {
        return "key";
    }
}
