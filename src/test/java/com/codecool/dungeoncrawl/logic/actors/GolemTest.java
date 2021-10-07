package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GolemTest {
    Golem golem;
    GameMap gameMap;

    @BeforeEach
    void setup() {
        gameMap = new GameMap(5, 5, CellType.FLOOR);
        golem = new Golem(gameMap.getCell(1, 1));
    }

    @Test
    void getTileName_resultGolem() {
        String exceptedName = "golem";
        assertEquals(exceptedName, golem.getTileName());
    }

    @Test
    void calculateAttack_regularAttack_resultAttack() {
        int expectedOutput = 1;
        assertEquals(expectedOutput, golem.calculateAttack());
    }
}
