package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Golem extends Enemy {

    public Golem(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "golem";
    }




}
