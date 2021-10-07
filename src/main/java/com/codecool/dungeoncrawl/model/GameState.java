package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GameState extends BaseModel {
    private Date savedAt;
    private GameMap currentMap;
    private List<String> discoveredMaps = new ArrayList<>();
    private PlayerModel player;
    private Cell[][] cells;

    public GameState(Cell[][] cells, PlayerModel player) {
        this.cells = cells;
        this.savedAt = new Date(System.currentTimeMillis());
        this.player = player;
    }

    public GameState(GameMap map, PlayerModel player) {
        this.currentMap = map;
        this.player = player;
    }

    public Date getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Date savedAt) {
        this.savedAt = savedAt;
    }

    public GameMap getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(GameMap currentMap) {
        this.currentMap = currentMap;
    }

    public List<String> getDiscoveredMaps() {
        return discoveredMaps;
    }

    public void addDiscoveredMap(String map) {
        this.discoveredMaps.add(map);
    }

    public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }

    public Cell[][] getCells() {
        return cells;
    }
}
