package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    protected Cell cell;
    private int health = 10;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = this.cell.getNeighbor(dx, dy);
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public void loseHealth(int healthChange){
        this.health -= healthChange;
        if (health<=0){
            this.cell.setActor(null);
        }
    }

    public int calculateDamage(int attack){
        return attack;
    }

    protected int calculateAttack(){
        return 1;
    }

    public void attack(Actor actor){
        int attack = calculateAttack();
        actor.calculateDamage(attack);
        actor.loseHealth(attack);
    }
}
