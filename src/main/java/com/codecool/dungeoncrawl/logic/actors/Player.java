package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.util.Inventory;

public class Player extends Actor {
    private final Inventory inventory = new Inventory();

    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    @Override
    public void move(int dx, int dy){
        Cell nextCell = this.cell.getNeighbor(dx, dy);
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL && (nextCell.getType() != CellType.CLOSEDDOOR || inventory.hasKey())) {
            if (nextCell.getType() == CellType.CLOSEDDOOR) {
                System.out.println(inventory);
                if (inventory.useKey()) {
                    System.out.println(inventory);
                    nextCell.setType(CellType.OPENDOOR);
                }
            }
            super.move(dx, dy);
        }
    }

    public void pickUpItem() {
        Cell cell = this.cell;
        Item item = cell.getItem();
        if (item != null) {
            cell.setItem(null);
            inventory.add(item);
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}
