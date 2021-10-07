package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateDaoJdbc implements GameStateDao {
    private DataSource dataSource;

    public GameStateDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(GameState state) {
        try (Connection conn = dataSource.getConnection()) {
            String sqlState = "INSERT INTO game_state (saved_at) VALUES (?)";
            PreparedStatement stateStatement = conn.prepareStatement(sqlState, Statement.RETURN_GENERATED_KEYS);
            stateStatement.setDate(1, state.getSavedAt());
            stateStatement.executeUpdate();
            ResultSet resultSet = stateStatement.getGeneratedKeys();
            resultSet.next();
            int stateId = resultSet.getInt(1);
            state.setId(stateId);
            Cell[][] cells = state.getCells();
            String sqlMap = "INSERT INTO map (width, height, state_id) VALUES (?, ?, ?)";
            PreparedStatement mapStatement = conn.prepareStatement(sqlMap, Statement.RETURN_GENERATED_KEYS);
            mapStatement.setInt(1, cells[0].length);
            mapStatement.setInt(2, cells.length);
            mapStatement.setInt(3, stateId);
            mapStatement.executeUpdate();
            resultSet = mapStatement.getGeneratedKeys();
            resultSet.next();
            int mapId = resultSet.getInt(1);
            int skeletonId = 1;
            int wizardId = 2;
            int golemId = 3;
            int swordId = 1;
            int keyId = 2;
            for (int i = 0; i < cells[0].length; i++) {
                for (int j = 0; j < cells.length; j++) {
                    Cell currentCell = cells[i][j];
                    String sqlCell = "INSERT INTO cell (type, x, y, map_id, enemy_id, item_id) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement cellStatement = conn.prepareStatement(sqlCell);
                    cellStatement.setString(1, currentCell.getTileName());
                    cellStatement.setInt(2, currentCell.getX());
                    cellStatement.setInt(3, currentCell.getY());
                    cellStatement.setInt(4, mapId);
                    if (currentCell.getActor() != null && !(currentCell.getActor() instanceof Player)) {
                        Actor enemy = currentCell.getActor();
                        switch (enemy.getTileName()) {
                            case "skeleton" -> cellStatement.setInt(5, skeletonId);
                            case "wizard" -> cellStatement.setInt(5, wizardId);
                            default -> cellStatement.setInt(5, golemId);
                        }
                    } else {
                        cellStatement.setNull(5, java.sql.Types.NULL);
                    }
                    if (currentCell.getItem() != null) {
                        Item item = currentCell.getItem();
                        switch (item.getTileName()) {
                            case "sword" -> cellStatement.setInt(6, swordId);
                            default -> cellStatement.setInt(6, keyId);
                        }
                    } else {
                        cellStatement.setNull(6, java.sql.Types.NULL);
                    }
                    cellStatement.executeUpdate();
                }
            }
            PlayerDao playerDao = new PlayerDaoJdbc(dataSource);
            playerDao.add(state.getPlayer());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {

    }

    @Override
    public GameState get(int id) {
        return null;
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }
}
