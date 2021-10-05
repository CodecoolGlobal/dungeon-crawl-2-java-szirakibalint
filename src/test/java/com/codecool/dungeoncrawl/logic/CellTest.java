package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Sword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CellTest {
    GameMap map;
    Cell cell;

    @BeforeEach
    void setup() {
        map = new GameMap(3, 3, CellType.FLOOR);
        cell = map.getCell(1, 1);
    }

    @Test
    void getNeighbor() {
        Cell neighbor = cell.getNeighbor(-1, 0);
        assertEquals(0, neighbor.getX());
        assertEquals(1, neighbor.getY());
    }

    @Test
    void cellOnEdgeHasNoNeighbor() {
        Cell cell = map.getCell(1, 0);
        assertEquals(null, cell.getNeighbor(0, -1));

        cell = map.getCell(1, 2);
        assertEquals(null, cell.getNeighbor(0, 1));
    }

    @Test
    void getItem_NoItemOnCell_ReturnNull() {
        assertNull(cell.getItem());
    }

    @Test
    void getItem_ItemSetOnCell_ReturnItem() {
        Item item = new Sword(cell);
        cell.setItem(item);
        assertEquals(item, cell.getItem());
    }

    @Test
    void getTileName_Floor_ReturnFloor() {
        String expectedTileName = "floor";
        assertEquals(expectedTileName, cell.getTileName());
    }

    @Test
    void getTileName_Wall_ReturnWall() {
        cell.setType(CellType.WALL);
        String expectedTileName = "wall";
        assertEquals(expectedTileName, cell.getTileName());
    }
}