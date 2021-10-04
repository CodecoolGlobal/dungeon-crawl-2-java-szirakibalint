package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Golem extends Enemy {

    public Golem(Cell cell) {
        super(cell);
        this.health = 50;
    }

    protected int calculateAttack(){
        return 1;
    }

    @Override
    public String getTileName() {
        return "golem";
    }

}
