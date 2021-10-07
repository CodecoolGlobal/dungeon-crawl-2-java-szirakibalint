package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.*;

public class PlayerTest {
    Player player;
    GameMap gameMap;
    Item item;

    @BeforeEach
    void setup() {
        gameMap = new GameMap(5, 5, CellType.FLOOR);
        player = new Player(gameMap.getCell(1, 1));
    }

    @Test
    void getTileName_TileNameWithIsAliveTrueWithNoSword_resultPlayer() {
        String expectedOutput = "player";
        assertEquals(expectedOutput, player.getTileName());
    }

    @Test
    void getTileName_TileNameWithIsAliveFalseWithNoSword_resultGhost() {
        player.loseHealth(player.health + 1);
        player.move(1,0);
        String expectedOutput = "ghost";
        assertEquals(expectedOutput, player.getTileName());
    }

    @Test
    void getTileName_TileNameWithIsAliveTrueWithSword_resultPlayerWithSword() {
        item = new Sword(gameMap.getCell(2,1));
        gameMap.getCell(2,1).setItem(item);
        player.move(1,0);
        player.pickUpItem();
        String expectedOutput = "playerwithsword";
        assertEquals(expectedOutput, player.getTileName());
    }

    @Test
    void pickUpItem_notSword_resultItemAddedToInventory() {
        item = new Key(gameMap.getCell(2,1));
        player.move(1,0);
        player.pickUpItem();
        assertFalse(player.hasSword());
        assertTrue(player.getInventory().getContent().size() == 1);
    }

    @Test
    void pickUpItem_sword_resultAddedToInventoryHasSwordTrue() {
        item = new Sword(gameMap.getCell(2,1));
        player.move(1,0);
        player.pickUpItem();
        assertTrue(player.hasSword());
        assertTrue(player.getInventory().getContent().stream().filter(e -> e instanceof Sword).collect(Collectors.toList()).size() == 1);
    }

    @Test
    void calculateAttack_beforePickingUpSword_resultRegularDamage() {
        assertEquals(player.getAttack(), player.calculateAttack());
    }

    @Test
    void calculateAttack_afterPickingUpSword_resultSwordDamage() {
        Sword sword = new Sword(gameMap.getCell(2,1));
        player.move(1,0);
        player.pickUpItem();
        int attackAfterPickingUpSword = player.calculateAttack();
        assertEquals(sword.getDamage(), attackAfterPickingUpSword);
    }

    @Test
    void move_playerWithKeyCanMoveTroughClosedDoor_resultCellTypeOpenDoor() {
        Item item = new Key(gameMap.getCell(2,1));
        gameMap.getCell(2,2).setType(CellType.CLOSEDDOOR);
        player.move(1,0);
        player.pickUpItem();
        player.move(0, 1);
        player.move(0, 1);
        assertTrue(gameMap.getCell(2,2).getType().equals(CellType.OPENDOOR));
    }

    @Test
    void move_playerAttackEnemy_resultHealthLoss() {
        Skeleton skeleton = new Skeleton(gameMap.getCell(2,1));
        int regularhealthSkeleton = skeleton.getHealth();
        int regularhealthPlayer = player.health;
        player.move(1,0);
        assertTrue(skeleton.health == regularhealthSkeleton - player.calculateAttack());
        if (skeleton.health < 0) {
            assertTrue(player.health == regularhealthPlayer - skeleton.calculateAttack());
        }
    }

    @Test
    void move_heartOnFloorWithPositiveHealth_resultHealthPlusTen() {
        gameMap.getCell(2,1).setType(CellType.HEARTONFLOOR);
        player.loseHealth(player.health - (player.health - 1));
        int healthBefore = player.health;
        player.move(1,0);
        assertEquals(healthBefore + 10, player.health);
    }

    @Test
    void move_heartOnFloorWithNegativeHealth_resultTenHealth() {
        gameMap.getCell(2,1).setType(CellType.HEARTONFLOOR);
        player.loseHealth(player.health + 1);
        player.move(1,0);
        assertEquals(10, player.health);

    }

    @Test
    void pickupItem_pickingUpItem_resultItemIsNotOnMap() {
        item = new Sword(gameMap.getCell(2,1));
        gameMap.getCell(2,1).setItem(item);
        player.move(1, 0);
        player.pickUpItem();
        player.move(-1, 0);
        assertNull(gameMap.getCell(2,1).getItem());
    }


}
