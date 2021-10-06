package com.codecool.dungeoncrawl.logic.items;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyTest {
    GameMap gameMap;
    Key key;

    @BeforeEach
    void setup() {
        gameMap = new GameMap(3, 3, CellType.FLOOR);
        key = new Key(gameMap.getCell(1, 1));
    }

    @Test
    void getTileName_Key_ReturnKey() {
        String expectedOutcome = "key";
        assertEquals(expectedOutcome, key.getTileName());
    }

    @Test
    void toString_Key_ReturnKey() {
        String expectedOutcome = "Key";
        assertEquals(expectedOutcome, key.toString());
    }
}
