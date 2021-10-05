package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.util.Inventory;

import java.util.List;

public class Player extends Actor {
    private final Inventory inventory = new Inventory();
    private boolean hasSword = false;
    protected boolean isAlive;
    private String name;

    public Player(Cell cell) {
        super(cell);
        isAlive = true;
        name = "Player";
    }

    @Override
    public String getTileName() {
        return isAlive ? (hasSword ? "playerwithsword" : "player") : "ghost";
    }

    @Override
    public void move(int dx, int dy){
        Cell nextCell = this.cell.getNeighbor(dx, dy);
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL && (nextCell.getType() != CellType.CLOSEDDOOR || inventory.hasKey())) {
            if (nextCell.getType() == CellType.CLOSEDDOOR) {
                if (inventory.useKey()) {
                    nextCell.setType(CellType.OPENDOOR);
                }
            }
            super.move(dx, dy);
        } if (nextCell.getType() == CellType.STAIRSDOWN) {
            Main.loadLevel("/map2.txt");
        } else if (nextCell.getActor() instanceof Enemy){
            attack(nextCell.getActor());
            if (nextCell.getActor() != null){
                nextCell.getActor().attack(this);
            }
        }
        isAlive = checkIsAlive();
    }

    @Override
    protected int calculateAttack(){
        List<Item> items = inventory.getContent().stream().filter(x -> x instanceof Sword).toList();
        int attack = 5;
        for (Item weapon: items){
            if (((Sword) weapon).getDamage() > attack){
                attack  = ((Sword) weapon).getDamage();
            }
        }
        return attack;
    }

    public void pickUpItem() {
        Cell cell = this.cell;
        Item item = cell.getItem();
        if (item != null) {
            cell.setItem(null);
            inventory.add(item);
            if (item instanceof Sword) hasSword = true;
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public boolean hasSword() {
        return hasSword;
    }

    public boolean checkIsAlive() {
        return (this.getHealth() > 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
