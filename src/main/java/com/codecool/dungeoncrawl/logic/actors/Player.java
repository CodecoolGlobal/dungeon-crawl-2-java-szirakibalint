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
    private boolean levelUp;

    private int attack = 5;

    public Player(Cell cell) {
        super(cell);
        isAlive = true;
        name = "Player";
    }

    public int getAttack() {
        return attack;
    }

    @Override
    public String getTileName() {
        return isAlive ? (hasSword ? "playerwithsword" : "player") : "ghost";
    }

    @Override
    public void move(int dx, int dy){
        Cell nextCell = this.cell.getNeighbor(dx, dy);
        if (nextCell == null){
            System.out.println("Target cell out of range");
        }
        else if (canMove(nextCell)) {
            if (nextCell.getType() == CellType.CLOSEDDOOR) {
                if (inventory.useKey()) {
                    nextCell.setType(CellType.OPENDOOR);
                }
            }
            super.move(dx, dy);
        } else if (nextCell.getType() == CellType.STAIRSDOWN) {
            levelUp = true;
        } else if (nextCell.getActor() instanceof Enemy){
            attack(nextCell.getActor());
            if (nextCell.getActor() != null){
                nextCell.getActor().attack(this);
            }
        }
        isAlive = checkIsAlive();
    }

    private boolean canMove(Cell nextCell) {
        return nextCell.getType() != CellType.STAIRSDOWN
                && nextCell.getActor() == null
                && nextCell.getType() != CellType.WALL
                && (nextCell.getType() != CellType.CLOSEDDOOR || inventory.hasKey());
    }

    @Override
    protected int calculateAttack(){
        List<Item> items = inventory.getContent().stream().filter(x -> x instanceof Sword).toList();
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

    public boolean shouldLevelUp() {
        return levelUp;
    }

    public void setLevelUp(boolean levelUp) {
        this.levelUp = levelUp;
    }
}
