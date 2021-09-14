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
        if (nextCell.getActor() == null && nextCell.getType() != CellType.WALL && (nextCell.getType() != CellType.CLOSEDDOOR || this.hasKey())) {
            if (nextCell.getType() == CellType.CLOSEDDOOR) {
                nextCell.setType(CellType.OPENDOOR);
            }
            super.move(dx, dy);
        }
    }
    public boolean hasKey() {
        //#TODO implement actual haskey method if available
        return true;
    }
}
