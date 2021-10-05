package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActorTest {
    GameMap gameMap;
    Player player;

    @BeforeEach
    void setPlayer() {
        gameMap = new GameMap(3, 3, CellType.FLOOR);
        player = new Player(gameMap.getCell(1, 1));
    }

    @Test
    void moveUpdatesCells() {
        player.move(1, 0);
        assertEquals(2, player.getX());
        assertEquals(1, player.getY());
        assertEquals(null, gameMap.getCell(1, 1).getActor());
        assertEquals(player, gameMap.getCell(2, 1).getActor());
    }

    @Test
    void cannotMoveIntoWall() {
        gameMap.getCell(2, 1).setType(CellType.WALL);
        player.move(1, 0);
        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    void cannotMoveOutOfMap() {
        for (int i = 0; i < 2; i++) {
            player.move(1, 0);
        }
        assertEquals(2, player.getX());
        assertEquals(1, player.getY());
    }

    @Test
    void cannotMoveIntoAnotherActor() {
        Skeleton skeleton = new Skeleton(gameMap.getCell(2, 1));
        player.move(1, 0);

        assertEquals(1, player.getX());
        assertEquals(1, player.getY());
        assertEquals(2, skeleton.getX());
        assertEquals(1, skeleton.getY());
        assertEquals(skeleton, gameMap.getCell(2, 1).getActor());
    }

    @Test
    void move_StepOnHeart_HealthChanged() {
        Cell cell = gameMap.getCell(2, 1);
        cell.setType(CellType.HEARTONFLOOR);
        player.move(1, 0);
        int expectedHealth = 20;
        assertEquals(expectedHealth, player.getHealth());
        assertEquals(CellType.FLOOR, cell.getType());
    }

    @Test
    void move_StepOnHeartNegativeHealth_HealthPositive() {
        Cell cell = gameMap.getCell(2, 1);
        cell.setType(CellType.HEARTONFLOOR);
        player.loseHealth(100);
        player.move(1, 0);
        int expectedHealth = 10;
        assertEquals(expectedHealth, player.getHealth());
        assertEquals(CellType.FLOOR, cell.getType());
    }

    @Test
    void getCell_PlayerOnFloor_ReturnFloorCell(){
        Cell expectedCell = gameMap.getCell(1, 1);
        Cell playerCell = player.getCell();
        assertEquals(expectedCell, playerCell);
    }
}