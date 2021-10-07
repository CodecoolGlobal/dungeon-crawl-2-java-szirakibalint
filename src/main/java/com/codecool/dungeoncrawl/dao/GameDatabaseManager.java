package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private PlayerItemsDaoJdbc playerItemsDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        playerItemsDao = new PlayerItemsDaoJdbc(dataSource);
    }

    public List<Item> getItemsForPlayer(PlayerModel playerModel) {
        int playerId = playerModel.getId();
        return playerItemsDao.get(playerId);
    }

    /*private void savePlayer(Player player) {
        PlayerModel model = new PlayerModel(player);
        playerDao.add(model, 0);
    }*/

    public PlayerModel loadPlayer(String name) {
        return playerDao.get(name);
    }

    public GameState loadGameState(int id, PlayerModel player) {
        return gameStateDao.get(id, player);
    }

    public void saveGameState(GameMap map, Player player) {
        GameState gameState = new GameState(map.getCells(), new PlayerModel(player));
        gameStateDao.add(gameState);
    }

    public List<PlayerModel> getAllPlayers() {
        return playerDao.getAll();
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("PSQL_DB_NAME");
        String user = System.getenv("PSQL_USER_NAME");
        String password = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }
}
