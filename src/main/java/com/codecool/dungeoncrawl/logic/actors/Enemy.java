package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public abstract class Enemy extends Actor{

    public Enemy(Cell cell) {
        super(cell);
    }

    @Override
    public String getTileName() {
        return "enemy";
    }

    @Override
    public void loseHealth(int healthChange){
        super.loseHealth(healthChange);
        if (health<=0){
                this.cell.setActor(null);
        }
    }
}
