package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.util.ItemSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javafx.stage.StageStyle;

import javax.swing.text.Position;


public class Main extends Application {
    static GameMap map = MapLoader.loadMap("/map.txt");
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    GameDatabaseManager dbManager;
    Label inventoryLabel = new Label("Inventory is empty");
    Button pickUpButton = new Button("Pick up");
    Button importButton = new Button("Import");
    Button exportButton = new Button("Export");

    public static void main(String[] args) {
        launch(args);
    }

    public static void loadLevel(String levelMap){
        Player player = map.getPlayer();
        map = MapLoader.loadMap(levelMap);

        Cell playerCell = map.getPlayer().getCell();
        playerCell.setActor(player);
        map.setPlayer(player);
        player.setCell(playerCell);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //setupDbManager();
        canvas.setFocusTraversable(false);
        pickUpButton.focusedProperty().addListener(e -> canvas.requestFocus());
        importButton.focusedProperty().addListener(e -> canvas.requestFocus());
        exportButton.focusedProperty().addListener(e -> canvas.requestFocus());
        setPickUpButtonClickEvent();
        setExportButtonClickEvent();
        setImportButtonClickEvent();

        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(pickUpButton, 0, 1);
        ui.add(inventoryLabel, 0, 2);

        GridPane ui2 = new GridPane();

        ui2.add(importButton, 0, 0);
        ui2.add(exportButton, 1, 0);

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        borderPane.setTop(ui2);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Private Static Final Fantasy");
        primaryStage.show();
        canvas.requestFocus();
    }

    private void setPickUpButtonClickEvent() {
        pickUpButton.setOnAction(e -> {
            Player player = map.getPlayer();
            player.pickUpItem();
            inventoryLabel.setText(player.getInventory().toString());
            refresh();
        });
    }

    private void setExportButtonClickEvent() {
        exportButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                System.out.println("success");
                System.out.println("File selected: " + selectedFile.getName());
                try {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer()).create();
                    String jsonString = gson.toJson(map);
                    System.out.println(jsonString);
                    FileWriter writer = new FileWriter(selectedFile);
                    gson.toJson(map, writer);
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else {
                System.out.println("File selection cancelled.");
            }
            fileChooser = new FileChooser();
            selectedFile = fileChooser.showSaveDialog(null);

            if (selectedFile != null) {
                System.out.println("success");
                System.out.println("File selected: " + selectedFile.getName());
                try {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer()).create();
                    String jsonString = gson.toJson(map.getPlayer());
                    System.out.println(jsonString);
                    FileWriter writer = new FileWriter(selectedFile);
                    gson.toJson(map.getPlayer(), writer);
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else {
                System.out.println("File selection cancelled.");
            }

        });
    }
    private void setImportButtonClickEvent() {
        importButton.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                System.out.println("success");
                System.out.println("File selected: " + selectedFile.getName());
                try {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer()).create();
                    JsonReader reader = new JsonReader(new FileReader(selectedFile));
                    map = gson.fromJson(reader, GameMap.class);
                    System.out.println(map);

                    for (Cell[] row : map.getCells()){
                        for (Cell cell: row){
                            cell.setGameMap(map);
                            if (cell.getActor() instanceof Player){
                                System.out.println(cell.getActor());
                            }
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else {
                System.out.println("File selection cancelled.");
            }
            fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                System.out.println("success");
                System.out.println("File selected: " + selectedFile.getName());
                try {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Item.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer())
                            .registerTypeAdapter(Actor.class,
                                    new ItemSerializer()).create();
                    JsonReader reader = new JsonReader(new FileReader(selectedFile));
                    map.setPlayer(gson.fromJson(reader, Player.class));
                    for (Cell[] row : map.getCells()){
                        for (Cell cell : row){
                            Actor actor = cell.getActor();
                            if (actor != null){

                                System.out.println(actor);
                                System.out.println(actor.getCell());

                                actor.setCell(cell);
                                if (actor instanceof Player){
                                    map.setPlayer((Player) actor);
                                }
                                System.out.println(actor.getCell());

                            }
                        }
                    }
                    inventoryLabel.setText(map.getPlayer().getInventory().toString());
                    refresh();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            else {
                System.out.println("File selection cancelled.");
            }

        });
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
            openSaveModal();
        }
    }

    private void initModalCancelButtonClickEvent(Stage modal, Button cancelButton) {
        cancelButton.setOnAction(e -> modal.close());
    }

    private boolean createOverwriteAlert(String enteredName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm overwrite");
        alert.setHeaderText(String.format("\"%s\" found in the database", enteredName));
        alert.setContentText("Would you like to overwrite the already existing state?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        boolean result;
        Optional<ButtonType> chosenButton = alert.showAndWait();
        ButtonType choice = chosenButton.orElse(noButton);
        if (choice.equals(yesButton)) {
            result = true;
        } else if (choice.equals(noButton)) {
            result = false;
        } else {
            result = false;
        }
        return result;
    }

    private void initModalSaveButtonClickEvent(Stage modal, Button saveButton, TextField nameField) {
        saveButton.setOnAction(e -> {
            String enteredName = nameField.getText();
//            TODO: replace with dbManager usage
//            List<PlayerModel> players = dbManager.getAllPlayers();
            List<PlayerModel> players = new ArrayList<>();
            for (PlayerModel player: players) {
                if (enteredName.equals(player.getPlayerName())) {
                    boolean overwrite = createOverwriteAlert(enteredName);
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

    private Stage createSaveModal() {
        Label label = new Label("Name:");
        TextField nameField = new TextField();
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");
        GridPane layout = new GridPane();

        nameField.setMinWidth(200);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setHgap(10);
        layout.setVgap(5);
        layout.setAlignment(Pos.CENTER);

        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(110);
        buttonContainer.getChildren().addAll(saveButton, cancelButton);

        layout.add(label, 0, 0);
        layout.add(nameField,0, 1);
        layout.add(buttonContainer, 0, 2);

        Stage modal = new Stage();
        Scene content = new Scene(layout, 250, 120);

        modal.setTitle("Save game");
        // modal.setAlwaysOnTop(true);
        modal.setScene(content);
        modal.setResizable(false);

        initModalCancelButtonClickEvent(modal, cancelButton);
        initModalSaveButtonClickEvent(modal, saveButton, nameField);
        return modal;
    }

    private void openSaveModal() {
        Stage modal = createSaveModal();
        modal.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                System.out.println(map);
                map.getPlayer().move(0, -1);
                enemyTurn();
                refresh();
                break;
            case DOWN:
                map.getPlayer().move(0, 1);
                enemyTurn();
                refresh();
                break;
            case LEFT:
                map.getPlayer().move(-1, 0);
                enemyTurn();
                refresh();
                break;
            case RIGHT:
                map.getPlayer().move(1,0);
                enemyTurn();
                refresh();
                break;
            case S:
                Player player = map.getPlayer();
                dbManager.savePlayer(player);
                break;
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

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
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
}
