package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameMapTest {

    GameMap gameMap;

    @BeforeEach
    void setupGameMap() {
        gameMap = new GameMap(3, 4, CellType.FLOOR);
    }

    @Test
    void gameMap_ThreeByFourMap_ReturnThreeWidthFourHeight() {
        int expectedHeight = 4;
        int expectedWidth = 3;
        assertEquals(expectedHeight, gameMap.getHeight());
        assertEquals(expectedWidth, gameMap.getWidth());
    }

    @Test
    void gameMap_AllFloorMap_ReturnFloorType() {
        for (int i = 0; i < gameMap.getWidth(); i++) {
            for (int j = 0; j < gameMap.getHeight(); j++) {
                assertEquals(CellType.FLOOR, gameMap.getCell(i, j).getType());
            }
        }
    }

    @Test
    void getPlayer_NoPlayerOnBoard_ReturnNull() {
        assertNull(gameMap.getPlayer());
    }

    @Test
    void getPlayer_PlayerOnBoard_ReturnPlayer() {
        Cell playerCell = gameMap.getCell(1, 1);
        Player player = new Player(playerCell);
        gameMap.setPlayer(player);
        assertEquals(player, gameMap.getPlayer());
    }
}
