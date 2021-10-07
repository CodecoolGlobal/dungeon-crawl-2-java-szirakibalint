package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Item implements Drawable {
    transient protected Cell cell;
    private String name;

    public Item(){

    }

    public Item(Cell cell) {
        this.cell = cell;
        this.cell.setItem(this);
        this.name = "Item";
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getTileName() {
        return "item";
    }
}
