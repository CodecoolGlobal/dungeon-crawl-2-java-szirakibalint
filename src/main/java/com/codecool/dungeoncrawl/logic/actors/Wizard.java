package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;

public class Wizard extends Enemy{

    private char movementDirection = 'h';

    public Wizard(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "wizard";
    }

    public int[] getNextMove(){

        int d = (int) Math.signum(0.5  - Math.random());

        if (movementDirection == 'h'){
            return new int[]{d, 0};
        } else {
            return new int[]{0, d};
        }
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
        for (int dx = -2; dx < 3; dx++) {
            for (int dy = -2; dy < 3; dy++){
                if ((dy == 0 )|| (dx == 0) || (Math.abs(dx) == Math.abs(dy))){
                    Actor actor = cell.getNeighbor(dx, dy).getActor();
                    if (actor instanceof Player){
                        attack(actor);
                    } else {
                        int[] move = getNextMove();
                        move(move[0], move[1]);
                    }
                }
            }
        }

    }
}
