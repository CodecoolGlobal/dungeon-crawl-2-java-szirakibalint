package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player, int stateId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (name, hp, x, y, state_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.setInt(5, stateId);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int playerId = resultSet.getInt(1);
            player.setId(playerId);
            List<Item> inventory = player.getInventory().getContent();
            int swordId = 1;
            int keyId = 2;
            for (Item item : inventory) {
                sql = "INSERT INTO player_item (player_id, item_id) VALUES (?, ?)";
                PreparedStatement itemStatement = conn.prepareStatement(sql);
                itemStatement.setInt(1, playerId);
                if (item.getTileName().equals("sword")) {
                    itemStatement.setInt(2, swordId);
                } else {
                    itemStatement.setInt(2, keyId);
                }
                itemStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET name = ?, hp = ?, x = ?, y = ? WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getHp());
            statement.setInt(3, player.getX());
            statement.setInt(4, player.getY());
            statement.setInt(5, player.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        return null;
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id, name, hp, x, y, state_id FROM player";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            List<PlayerModel> players = new ArrayList<>();
            while (resultSet.next()) {
                int playerId = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int hp = resultSet.getInt(3);
                int x = resultSet.getInt(4);
                int y = resultSet.getInt(5);
                int stateId = resultSet.getInt(6);
                PlayerModel player = new PlayerModel(name, x, y);
                player.setHp(hp);
                player.setStateId(stateId);
                player.setId(playerId);
                players.add(player);
            }
            return players;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
