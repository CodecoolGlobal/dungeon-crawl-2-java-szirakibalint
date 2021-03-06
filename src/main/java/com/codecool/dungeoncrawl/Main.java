package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.util.ItemSerializer;
import com.codecool.dungeoncrawl.model.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.codecool.dungeoncrawl.logic.util.JsonHandler;
import com.codecool.dungeoncrawl.logic.util.UiHandler;
import com.codecool.dungeoncrawl.model.PlayerModel;
import com.google.gson.JsonSyntaxException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    GameMap map = MapLoader.loadMap("/map.txt");
    JsonHandler jsonHandler = new JsonHandler();
    private UiHandler uiHandler = new UiHandler();

    GameDatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }

    public void loadLevel(String levelMap) {
        Player player = map.getPlayer();
        map = MapLoader.loadMap(levelMap);

        Cell playerCell = map.getPlayer().getCell();
        playerCell.setActor(player);
        map.setPlayer(player);
        player.setCell(playerCell);
    }

    @Override
    public void start(Stage primaryStage) {
        setupDbManager();

        uiHandler.initCanvas(map.getWidth() * Tiles.TILE_WIDTH,
                map.getHeight() * Tiles.TILE_WIDTH);

        uiHandler.initButtons(this);

        BorderPane window = uiHandler.setUpWindow();

        Scene scene = new Scene(window);
        primaryStage.setScene(scene);
        uiHandler.refresh(map);
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Private Static Final Fantasy");
        primaryStage.show();
        uiHandler.getCanvas().requestFocus();
    }


    public Stage createListView() {
        Button loadButton = new Button("Load");
        ListView<String> listView = new ListView<>();
        List<PlayerModel> players = dbManager.getAllPlayers();
        for (PlayerModel player: players) {
            listView.getItems().add(player.getPlayerName());
        }
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        VBox layout = new VBox(5);
        layout.getChildren().addAll(listView, loadButton);
        Stage modal = new Stage();
        modal.setTitle("Saves in the database");
        Scene content = new Scene(layout, 400, 500);
        modal.setScene(content);
        loadButton.setOnAction(e -> {
            String selectedName = listView.getSelectionModel().getSelectedItem();
            Player currentPlayer = map.getPlayer();
            currentPlayer.setName(selectedName);
            loadFromDB();
            modal.close();
        });
        return modal;
    }

    public void showListView() {
        createListView().show();
    }

    public void setPickUpButtonClickEvent() {
        uiHandler.getPickUpButton().setOnAction(e -> {
            Player player = map.getPlayer();
            player.pickUpItem();
            uiHandler.refresh(map);
        });
    }

    public void setExportButtonClickEvent() {
        uiHandler.getExportButton().setOnAction(e -> jsonHandler.exportGame(map));
    }

    public void setImportButtonClickEvent() {
        uiHandler.getImportButton().setOnAction(e -> {
                    GameMap importedGame;
                    boolean tryAgain = true;
                    while (tryAgain) {
                        try {
                            importedGame = jsonHandler.importGame();
                            if (importedGame != null) {
                                this.map = importedGame;
                                uiHandler.refresh(map);
                            }
                            tryAgain = false;
                        } catch (JsonSyntaxException ex) {
                            tryAgain = uiHandler.showBadJsonDialog();
                        }
                    }
                }
        );
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        KeyCombination saveCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        } else if (saveCombination.match(keyEvent)) {
            uiHandler.openSaveModal(this);
        }
    }

    public void initModalSaveButtonClickEvent(Stage modal, Button saveButton, TextField nameField) {
        saveButton.setOnAction(e -> {
            String enteredName = nameField.getText();
            Player currentPlayer = map.getPlayer();
            currentPlayer.setName(enteredName);
            List<PlayerModel> players = dbManager.getAllPlayers();
            for (PlayerModel player : players) {
                if (enteredName.equals(player.getPlayerName())) {
                    boolean overwrite = uiHandler.createOverwriteAlert(enteredName);
                    if (overwrite) {
                        dbManager.deletePlayer(currentPlayer);
                        dbManager.saveGameState(map, currentPlayer);
                        modal.close();
                    }
                    return;
                }
            }
            dbManager.saveGameState(map, currentPlayer);
            modal.close();
        });
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        Player player = map.getPlayer();
        int dx = 0;
        int dy = 0;
        switch (keyEvent.getCode()) {
            case UP:
                dy = -1;
                break;
            case DOWN:
                dy = 1;
                break;
            case LEFT:
                dx = -1;
                break;
            case RIGHT:
                dx = 1;
                break;
//            case S:
//                dbManager.saveGameState(map, map.getPlayer());
//                break;
            case L:
                showListView();
                break;
            case R:
                loadFromDB();
                break;
        }
        if (dx + dy != 0) {
            player.move(dx, dy);
            if (player.shouldLevelUp()) {
                loadLevel("/map2.txt");
                player.setLevelUp(false);
            } else {
                enemyTurn();
            }
            uiHandler.refresh(map);
        }
    }

    private void enemyTurn() {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null && cell.getActor() != map.getPlayer()) {
                    cell.getActor().act();
                }
            }
        }
    }

    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void loadFromDB() {
        Player currentPlayer = map.getPlayer();
        PlayerModel model = dbManager.loadPlayer(currentPlayer.getName());
        GameState state = dbManager.loadGameState(model.getStateId(), model);
        map = state.getCurrentMap();
        Player player = new Player(map.getCell(model.getX(), model.getY()));
        player.setHealth(model.getHp());
        player.setName(model.getPlayerName());
        List<Item> items = model.getInventory().getContent();
        for (Item item : items) {
            player.getInventory().add(item);
        }
        player.calculateAttack();
        map.getCell(model.getX(), model.getY()).setActor(player);
        map.setPlayer(player);
        uiHandler.refresh(map);
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
