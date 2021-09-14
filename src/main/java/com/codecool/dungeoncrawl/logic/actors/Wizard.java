package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Wizard extends Enemy{

    public Wizard(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "wizard";
    }

    public void act(){
        for (int dx = -2; dx < 3; dx++) {
            for (int dy = -2; dy < 3; dy++){
                if ((dy == 0 )|| (dx == 0) || (Math.abs(dx) == Math.abs(dy))){
                    Actor actor = cell.getNeighbor(dx, dy).getActor();
                    if (actor instanceof Player){
                        attack(actor);
                    }
                }
            }
        }

    }
}
