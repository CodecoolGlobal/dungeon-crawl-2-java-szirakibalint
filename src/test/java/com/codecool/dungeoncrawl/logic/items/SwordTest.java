package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SwordTest {

    GameMap gameMap;
    Sword sword;

    @BeforeEach
    void setupFields() {
        gameMap = new GameMap(3, 3, CellType.FLOOR);
        sword = new Sword(gameMap.getCell(1, 1));
        gameMap.getCell(1, 1).setItem(sword);
    }

    @Test
    void getDamage_SwordHasSixDamage_ReturnSix() {
        int expectedDamage = 6;
        assertEquals(expectedDamage, sword.getDamage());
    }

    @Test
    void getTileName_Sword_ReturnSword() {
        String expectedTileName = "sword";
        assertEquals(expectedTileName, sword.getTileName());
    }

    @Test
    void toString_Sword_ReturnSword() {
        String expectedOutcome = "Sword";
        assertEquals(expectedOutcome, sword.toString());
    }
}
