package com.codecool.dungeoncrawl.logic.actors;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class SkeletonTest {
    GameMap gameMap;
    Skeleton skeleton;

    @BeforeEach
    void setSkeleton() {
        gameMap = new GameMap(5, 5, CellType.FLOOR);
        skeleton = new Skeleton(gameMap.getCell(1, 1));
    }

    @Test
    void getTileName_resultSkeletonTilename() {
        String exceptedTileName = "skeleton";
        assertEquals(exceptedTileName, skeleton.getTileName());
    }

    @Test
    void getNextMove_ReturnsValidResult(){
        int [] moves = skeleton.getNextMove();
        assertTrue(Arrays.stream(moves).allMatch(cell -> cell == 1 || cell == -1));
    }

    @Test
    void move_UpdateCells() {
        skeleton.move(1,0);
        assertEquals(2, skeleton.getX());
        assertEquals(1, skeleton.getY());
        assertEquals(null, gameMap.getCell(1, 1).getActor());
        assertEquals(skeleton, gameMap.getCell(2, 1).getActor());
    }



}
