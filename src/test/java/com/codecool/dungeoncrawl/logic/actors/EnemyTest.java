package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {
    GameMap gameMap;
    Enemy enemy;

    @BeforeEach
    void setEnemy() {
        gameMap = new GameMap(3, 3, CellType.FLOOR);
        enemy = new Skeleton(gameMap.getCell(1, 1));
    }

    @Test
    void getTileName_returnInstanceOfEnemy_notEnemy() {
        assertFalse(enemy.getTileName().equals("enemy"));
    }
}
