package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    transient protected Cell cell;
    protected int health = 10;

    public Actor(){} //needed for deserialization to work

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = this.cell.getNeighbor(dx, dy);

        if (nextCell != null){
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
            if ((cell.getType() == CellType.HEARTONFLOOR) && (this instanceof Player)) {
                health = health > 0 ? health + 10 : 10;
                this.cell.setType(CellType.FLOOR);
            }
        } else {
            System.out.println("Target cell out of range");
        }
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

    }

    public int calculateDamage(int attack){
        return attack;
    }

    protected int calculateAttack(){
        return 2;
    }

    public void attack(Actor actor){
        int attack = calculateAttack();
        int damage = actor.calculateDamage(attack);
        actor.loseHealth(damage);
    }

    public void act(){}

    public void setCell(Cell cell){
        this.cell = cell;
    }
}
