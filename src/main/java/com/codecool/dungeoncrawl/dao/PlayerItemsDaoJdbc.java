package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.logic.util.Inventory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        try (Connection conn = dataSource.getConnection()) {
            String sql = """
                    SELECT item.id, item.name
                    FROM item
                    LEFT JOIN player_item pi ON item.id = pi.item_id
                    LEFT JOIN player p ON p.id = pi.player_id
                    WHERE player_id = ?
                    """;
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, player_id);
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<Item> items = new ArrayList<>();
            while (resultSet.next()) {
                String itemName = resultSet.getString(2);
                switch (itemName) {
                    case "sword" -> items.add(new Sword());
                    default -> items.add(new Key());
                }
            }
            return items;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
