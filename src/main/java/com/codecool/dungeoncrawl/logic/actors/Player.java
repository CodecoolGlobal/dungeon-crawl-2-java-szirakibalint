package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;

import java.util.ArrayList;

public class Player extends Actor {
    ArrayList<Item> items = new ArrayList<>();
    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    @Override
    public void move(int dx, int dy){
        Cell nextCell = this.cell.getNeighbor(dx, dy);
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL && (nextCell.getType() != CellType.CLOSEDDOOR || hasKey())) {
            if (nextCell.getType() != CellType.CLOSEDDOOR) {
                useKey();
            }
            if (nextCell.getItem().getTileName() == "key") {
                items.add(nextCell.getItem());
            }
            super.move(dx, dy);
        }
    }

    public boolean hasKey() {
        for (Item elem : items) {
            if (elem instanceof Key) {
                return true;
            }
        }
        return false;
    }

    public void useKey() {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof Key) {
                items.remove(i);
            }
        }
    }

    public void pickUpItem() {
        Cell cell = this.cell;
        if (cell.getItem() != null) {
            cell.setItem(null);
        }
    }
}
