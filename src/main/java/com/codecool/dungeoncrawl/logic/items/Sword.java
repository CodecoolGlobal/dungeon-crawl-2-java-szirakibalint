package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;

public class Sword extends Item {
    private int damage = 6;

    public Sword(Cell cell) {
        super(cell);
        super.setName("Sword");
    }

    public Sword() {
        super.setName("Sword");
    }

    @Override
    public String getTileName() {
        return "sword";
    }

    public int getDamage() {
        return damage;
    }
}
