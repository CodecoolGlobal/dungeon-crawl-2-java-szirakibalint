package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Skeleton extends Enemy {
    public Skeleton(Cell cell) {
        super(cell);
        this.health = 8;
    }

    @Override
    public String getTileName() {
        return "skeleton";
    }

    public int[] getNextMove(){

        int dx = (int) Math.signum(0.5  - Math.random());
        int dy = (int) Math.signum(0.5  - Math.random());

        return new int[]{dx, dy};

    }

    @Override
    public void move(int dx, int dy){
        Cell nextCell = this.cell.getNeighbor(dx, dy);
        if (nextCell.getActor() == null
                && nextCell.getType() != CellType.WALL
                && nextCell.getItem() == null
        ) {
            super.move(dx, dy);
        }
    }

    @Override
    public void act(){
        int[] move = getNextMove();
        move(move[0], move[1]);
    }


}
