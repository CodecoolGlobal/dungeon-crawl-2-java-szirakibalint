package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerDao {
    void add(PlayerModel player, int gameStateId);
    void update(PlayerModel player);
    PlayerModel get(String name);
    List<PlayerModel> getAll();
    void delete(String name);
}
