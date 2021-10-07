package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.util.JsonHandler;
import com.codecool.dungeoncrawl.logic.util.UiHandler;
import com.codecool.dungeoncrawl.model.PlayerModel;
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

    public void loadLevel(String levelMap){
        Player player = map.getPlayer();
        map = MapLoader.loadMap(levelMap);

        Cell playerCell = map.getPlayer().getCell();
        playerCell.setActor(player);
        map.setPlayer(player);
        player.setCell(playerCell);
    }

    @Override
    public void start(Stage primaryStage) {
        //setupDbManager();

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
        uiHandler.getImportButton().setOnAction(e ->
                {
                    GameMap importedGame = jsonHandler.importGame();
                    if (importedGame != null) {
                        this.map = jsonHandler.importGame();
                        uiHandler.refresh(map);
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
//            TODO: replace with dbManager usage
//            List<PlayerModel> players = dbManager.getAllPlayers();
            List<PlayerModel> players = new ArrayList<>();
            for (PlayerModel player: players) {
                if (enteredName.equals(player.getPlayerName())) {
                    boolean overwrite = uiHandler.createOverwriteAlert(enteredName);
                    if (overwrite) {
                        // TODO: Overwrite save in db
                        modal.close();
                    }
                } else {
                    // TODO: Create save in db
                }
            }
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
            case S:
                //dbManager.savePlayer(player);
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

    private void enemyTurn(){
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

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap newMap){
        map = newMap;
    }
}
