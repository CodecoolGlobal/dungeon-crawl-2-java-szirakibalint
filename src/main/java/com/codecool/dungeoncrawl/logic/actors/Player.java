package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Player extends Actor {
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



}
