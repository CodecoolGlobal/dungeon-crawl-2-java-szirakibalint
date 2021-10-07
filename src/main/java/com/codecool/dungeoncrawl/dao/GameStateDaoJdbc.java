package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.Sword;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            mapStatement.setInt(1, cells.length);
            mapStatement.setInt(2, cells[0].length);
            mapStatement.setInt(3, stateId);
            mapStatement.executeUpdate();
            resultSet = mapStatement.getGeneratedKeys();
            resultSet.next();
            int mapId = resultSet.getInt(1);
            int swordId = 1;
            int keyId = 2;
            for (int i = 0; i < cells.length; i++) {
                for (int j = 0; j < cells[0].length; j++) {
                    Cell currentCell = cells[i][j];
                    String sqlCell = "INSERT INTO cell (type, x, y, map_id, enemy_id, item_id) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement cellStatement = conn.prepareStatement(sqlCell);
                    cellStatement.setString(1, currentCell.getTileName());
                    cellStatement.setInt(2, currentCell.getX());
                    cellStatement.setInt(3, currentCell.getY());
                    cellStatement.setInt(4, mapId);
                    if (currentCell.getActor() != null && !(currentCell.getActor() instanceof Player)) {
                        Actor enemy = currentCell.getActor();
                        String sqlEnemy = "INSERT INTO enemy (hp, type) VALUES (?, ?)";
                        PreparedStatement enemyStatement = conn.prepareStatement(sqlEnemy, Statement.RETURN_GENERATED_KEYS);
                        enemyStatement.setInt(1, enemy.getHealth());
                        enemyStatement.setString(2, enemy.getTileName());
                        enemyStatement.executeUpdate();
                        resultSet = enemyStatement.getGeneratedKeys();
                        resultSet.next();
                        int enemyId = resultSet.getInt(1);
                        cellStatement.setInt(5, enemyId);
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
            playerDao.add(state.getPlayer(), stateId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(GameState state) {

    }

    @Override
    public GameState get(int stateId, PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = """
                SELECT
                    cell.id,
                    cell.type celltype,
                    x,
                    y,
                    e.type enemytype,
                    e.hp enemyhp,
                    item_id,
                    i.name itemname,
                    m.width,
                    m.height,
                    saved_at
                FROM cell
                LEFT JOIN map m ON cell.map_id = m.id
                LEFT JOIN game_state s ON m.state_id = s.id
                LEFT JOIN item i ON i.id = cell.item_id
                LEFT JOIN enemy e ON cell.enemy_id = e.id
                WHERE state_id = ?
                ORDER BY x, y;
                """;
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, stateId);
            ResultSet resultSet = statement.executeQuery();
            int width;
            int height;
            if (!resultSet.next()) {
                return null;
            }
            width = resultSet.getInt("width");
            height = resultSet.getInt("height");
            GameMap map = new GameMap(width, height, CellType.FLOOR);
            map = buildCell(resultSet, map);
            while (resultSet.next()) {
                map = buildCell(resultSet, map);
            }
            GameState gameState = new GameState(map, player);
            return gameState;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameState> getAll() {
        return null;
    }

    private GameMap buildCell(ResultSet resultSet, GameMap gameMap) {
        try {
            Cell cell = gameMap.getCell(resultSet.getInt("x"), resultSet.getInt("y"));
            cell.setType(CellType.valueOf(resultSet.getString("celltype").toUpperCase()));
            if (resultSet.getString("enemytype") != null) {
                Enemy enemy;
                switch (resultSet.getString("enemytype")) {
                    case "skeleton":
                        enemy = new Skeleton(cell);
                        break;
                    case "wizard":
                        enemy = new Wizard(cell);
                        break;
                    default:
                        enemy = new Golem(cell);
                }
                enemy.setHealth(resultSet.getInt("enemyhp"));
                cell.setActor(enemy);
            }
            if (resultSet.getString("itemname") != null) {
                if (resultSet.getString("itemname").equals("sword")) {
                    cell.setItem(new Sword());
                } else {
                    cell.setItem(new Key());
                }
            }
            return gameMap;
        } catch (SQLException sqle) {
            throw new RuntimeException("Error during building a cell");
        }
    }
}
