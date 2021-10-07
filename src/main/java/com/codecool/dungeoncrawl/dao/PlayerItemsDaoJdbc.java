package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.util.Inventory;

import javax.sql.DataSource;
import java.util.List;

public class PlayerItemsDaoJdbc implements PlayerItemsDao {
    private DataSource dataSource;

    public PlayerItemsDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(Inventory inventory, int playerId) {
    }

    @Override
    public void update(Inventory inventory, int playerId) {

    }

    @Override
    public List<Item> get(int player_id) {
        return null;
    }
}
