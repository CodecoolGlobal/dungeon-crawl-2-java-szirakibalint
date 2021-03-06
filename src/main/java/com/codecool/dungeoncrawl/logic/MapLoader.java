package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Golem;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Skeleton;
import com.codecool.dungeoncrawl.logic.actors.Wizard;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String fileName) {
        InputStream is = MapLoader.class.getResourceAsStream(fileName);
        assert is != null;
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            new Skeleton(cell);
                            break;
                        case 'w':
                            cell.setType(CellType.FLOOR);
                            new Wizard(cell);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            new Golem(cell);
                            break;
                        case 'i':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case '-':
                            cell.setType(CellType.CLOSEDDOOR);
                            break;
                        case 'd':
                            cell.setType(CellType.STAIRSDOWN);
                            break;
                        case 'r':
                            cell.setType(CellType.REDFLOWER);
                            break;
                        case 'y':
                            cell.setType(CellType.YELLOWFLOWER);
                            break;
                        case 'h':
                            cell.setType(CellType.HEARTONFLOOR);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
