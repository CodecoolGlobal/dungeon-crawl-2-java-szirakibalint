package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Wizard extends Enemy{

    public Wizard(Cell cell) {
        super(cell);
    }

    public void act(){


        if (cell.getNeighbor(-1, -1).getActor() instanceof Player){
            this.attack(cell.getNeighbor(-1, -1).getActor() );
        }
    }
}
