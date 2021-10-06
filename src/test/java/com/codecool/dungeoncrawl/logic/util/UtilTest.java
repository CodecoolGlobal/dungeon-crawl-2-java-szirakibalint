package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Key;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UtilTest {
    Player player;
    GameMap gameMap;

    @BeforeEach
    void setup() {
        gameMap = new GameMap(5, 5, CellType.FLOOR);
        player = new Player(gameMap.getCell(1, 1));
    }



    @Test
    void hasKey_withNoKey_resultFalse() {
        assertFalse(player.getInventory().hasKey());
    }

    @Test
    void hasKey_withKey_resultTrue() {
        player.getInventory().add(new Key(gameMap.getCell(1,1)));
        assertTrue(player.getInventory().hasKey());
    }

    @Test
    void useKey_withNoKey_returnsFalse() {
        assertFalse(player.getInventory().useKey());
    }

    @Test
    void useKey_PlayerHasTwoKeysLosesOneAfterUse_resultOneKey() {
        player.getInventory().add(new Key(gameMap.getCell(1,1)));
        player.getInventory().add(new Key(gameMap.getCell(2,1)));
        player.getInventory().useKey();
        assertTrue(player.getInventory().hasKey());

    }

    @Test
    void emptyInventory_displaysEmptyInventory() {
        String exceptedOutput = "Inventory is empty";
        assertEquals(exceptedOutput, player.getInventory().toString());
    }
}
