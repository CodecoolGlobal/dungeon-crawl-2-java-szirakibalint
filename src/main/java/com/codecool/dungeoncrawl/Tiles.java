package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(1, 1));
        tileMap.put("floor", new Tile(5, 0));
        tileMap.put("player", new Tile(25, 0));
        tileMap.put("playerwithsword", new Tile(25, 1));
        tileMap.put("ghost", new Tile(26, 6));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("closedDoor", new Tile(5, 10));
        tileMap.put("openDoor", new Tile(8, 10));
        tileMap.put("sword", new Tile(0, 30));
        tileMap.put("key", new Tile(16, 23));
        tileMap.put("wizard", new Tile(26, 9));
        tileMap.put("golem", new Tile(30, 6));
        tileMap.put("stairsDown", new Tile(2, 6));
        tileMap.put("heartonfloor", new Tile(22, 22));
        tileMap.put("yellowflower", new Tile(16, 6));
        tileMap.put("redflower", new Tile(15, 6));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
