package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class WizardTest {
    GameMap gameMap;
    Wizard wizard;
    Player player;

    @BeforeEach
    void setWizzard() {
        gameMap = new GameMap(5, 5, CellType.FLOOR);
        wizard = new Wizard(gameMap.getCell(1, 1));
    }

    @Test
    void getTileName_resultWizardTilename() {
        String exceptedTileName = "wizard";
        assertEquals(exceptedTileName, wizard.getTileName());
    }

    @Test
    void getNextMove_movementDirectionHorizontal_resultHorizontalMove() {
        int [] move = wizard.getNextMove();
        assertTrue(move[0] == 1 || move[0] == -1);
    }

    @Test
    void getNextMove_movementDirectionVertical_resultVerticalMove() {
        char direction = wizard.getMovementDirection();
        int [] move = wizard.getNextMove();
        while (direction != 'v') {
            move = wizard.getNextMove();
            direction = wizard.getMovementDirection();
        }
        move = wizard.getNextMove();
        assertTrue(move[1] == 1 || move[1] == -1);
    }

    @Test
    void moveDirection_changedAfterGetNextMove_resultChangeDirection() {
        char directionBeforeChange = wizard.getMovementDirection();
        wizard.getNextMove();
        char directionAfterChange = wizard.getMovementDirection();
        assertTrue((directionBeforeChange == 'h' && directionAfterChange == 'v') || (directionBeforeChange == 'v' && directionAfterChange == 'h'));
    }

    @Test
    void move_regularMove_ResultMoveWizard() {
        wizard.move(1, 0);
        assertEquals(wizard ,gameMap.getCell(2, 1).getActor());
    }

    @Test
    void move_wizardCannotMoveToWall_resultNoMove() {
        gameMap.getCell(2,1).setType(CellType.WALL);
        wizard.move(1,0);
        assertEquals(null ,gameMap.getCell(2, 1).getActor());
    }

    @Test
    void act_noPlayerOnMap_ResultRegularMove() {
        wizard.act();
        assertNull(gameMap.getCell(1,1).getActor());
    }

    @Test
    void act_playerOnMapWizzardAttacks_ResultPlayerHealthLoss() {
        player = new Player(gameMap.getCell(2, 1));
        int healthBeforeAttack = player.health;
        wizard.act();
        assertTrue(healthBeforeAttack - wizard.calculateAttack() == player.health);


    }









}
