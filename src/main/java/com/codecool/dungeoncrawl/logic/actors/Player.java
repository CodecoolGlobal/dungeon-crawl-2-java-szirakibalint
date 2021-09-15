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
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL) {
            super.move(dx, dy);
        }

        if (nextCell.getActor() instanceof Enemy){
            attack(nextCell.getActor());
            if (nextCell.getActor() != null){
                nextCell.getActor().attack(this);
            }
        }
    }

    @Override
    protected int calculateAttack(){
        return 5;
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
