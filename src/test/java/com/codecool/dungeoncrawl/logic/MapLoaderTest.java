package com.codecool.dungeoncrawl.logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTest {

    @Test
    void loadMap_LoadDummyMap_NoExceptionsThrown() {
        assertAll(() -> MapLoader.loadMap("/testmap.txt"));
    }

    @Test
    void loadMap_LoadDummyMap_EveryCellHasProperTile() {
        GameMap gameMap = MapLoader.loadMap("/testmap.txt");
        Cell empty = gameMap.getCell(0, 0);
        Cell floor = gameMap.getCell(3, 1);
        Cell wall = gameMap.getCell(1, 1);
        Cell closedDoor = gameMap.getCell(4, 1);
        Cell redFlower = gameMap.getCell(4, 3);
        Cell yellowFlower = gameMap.getCell(2, 4);
        Cell heartOnFloor = gameMap.getCell(3, 4);
        assertEquals(CellType.EMPTY, empty.getType());
        assertEquals(CellType.FLOOR, floor.getType());
        assertEquals(CellType.WALL, wall.getType());
        assertEquals(CellType.CLOSEDDOOR, closedDoor.getType());
        assertEquals(CellType.REDFLOWER, redFlower.getType());
        assertEquals(CellType.YELLOWFLOWER, yellowFlower.getType());
        assertEquals(CellType.HEARTONFLOOR, heartOnFloor.getType());
    }

}
