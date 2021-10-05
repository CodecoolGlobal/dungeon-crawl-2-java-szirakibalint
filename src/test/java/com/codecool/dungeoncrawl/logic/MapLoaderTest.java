package com.codecool.dungeoncrawl.logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MapLoaderTest {

    GameMap gameMap;

    @BeforeEach
    void setupMap() {
        gameMap = MapLoader.loadMap("/testmap.txt");
    }

    @Test
    void loadMap_LoadDummyMap_NoExceptionsThrown() {
        assertAll(() -> MapLoader.loadMap("/testmap.txt"));
    }

    @Test
    void loadMap_LoadDummyMap_EveryCellHasProperTile() {
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

    @Test
    void loadMap_LoadDummyMap_ItemsAreOnRightPlaces() {
        Cell floorEmpty = gameMap.getCell(3, 1);
        Cell key = gameMap.getCell(2, 3);
        Cell sword = gameMap.getCell(3, 3);
        assertNull(floorEmpty.getItem());
        assertNotNull(key.getItem());
        assertNotNull(sword);
    }

    @Test
    void loadMap_LoadDummyMap_EnemiesAreOnSpot() {
        Cell floorEmpty = gameMap.getCell(3, 1);
        Cell skeleton = gameMap.getCell(2, 2);
        Cell wizard = gameMap.getCell(3, 2);
        Cell golem = gameMap.getCell(4, 4);
        assertNull(floorEmpty.getActor());
        assertNotNull(skeleton.getActor());
        assertNotNull(wizard.getActor());
        assertNotNull(golem.getActor());
    }

    @Test
    void loadMap_LoadDummyMap_PlayerIsOnSpot() {
        Cell playerCell = gameMap.getCell(2, 1);
        assertNotNull(playerCell.getActor());
        assertNotNull(gameMap.getPlayer());
    }

    @Test
    void loadMap_LoadInvalidMap_RunTimeExceptionThrown() {
        assertThrows(RuntimeException.class, () -> MapLoader.loadMap("/invalidtestmap.txt"));
    }
}
