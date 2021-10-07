package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.util.Inventory;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerItemsDao {
    void add(Inventory inventory, int playerId);
    void update(Inventory inventory, int playerId);
    List<Item> get(int player_id);
}
