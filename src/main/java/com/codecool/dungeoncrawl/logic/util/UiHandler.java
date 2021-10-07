package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.Tiles;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Optional;

public class UiHandler {

    Label inventoryLabel = new Label("Inventory is empty");
    private Button pickUpButton = new Button("Pick up");
    private Button importButton = new Button("Import");
    private Button exportButton = new Button("Export");
    Label healthLabel = new Label();
    private Canvas canvas;
    GraphicsContext context;

    public void initCanvas(int width, int height){
        canvas = new Canvas(
                width,
                height);
        canvas.setFocusTraversable(false);
        context = canvas.getGraphicsContext2D();
    }

    public GridPane setUpSidebar() {
        GridPane sidebar = new GridPane();
        sidebar.setPrefWidth(200);
        sidebar.setPadding(new Insets(10));

        sidebar.add(new Label("Health: "), 0, 0);
        sidebar.add(healthLabel, 1, 0);
        sidebar.add(pickUpButton, 0, 1);
        sidebar.add(inventoryLabel, 0, 2);
        return sidebar;
    }

    public BorderPane setUpWindow() {
        GridPane sidebar = setUpSidebar();
        GridPane topBar = setUpTopBar();
        BorderPane window = new BorderPane();

        window.setCenter(canvas);
        window.setRight(sidebar);
        window.setTop(topBar);
        return window;
    }

    public void initButtons(Main main) {
        pickUpButton.focusedProperty().addListener(e -> canvas.requestFocus());
        importButton.focusedProperty().addListener(e -> canvas.requestFocus());
        exportButton.focusedProperty().addListener(e -> canvas.requestFocus());
        main.setPickUpButtonClickEvent();
        main.setExportButtonClickEvent();
        main.setImportButtonClickEvent();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GridPane setUpTopBar() {
        GridPane topBar = new GridPane();

        topBar.add(importButton, 0, 0);
        topBar.add(exportButton, 1, 0);
        return topBar;
    }

    public Button getPickUpButton() {
        return pickUpButton;
    }

    public Button getImportButton() {
        return importButton;
    }

    public Button getExportButton() {
        return exportButton;
    }

    public void refresh(GameMap map) {
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
        inventoryLabel.setText(map.getPlayer().getInventory().toString());
    }

    public Stage createSaveModal(Main main) {
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
        main.initModalSaveButtonClickEvent(modal, saveButton, nameField);
        return modal;
    }

    public boolean createOverwriteAlert(String enteredName) {
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

    public void openSaveModal(Main main) {
        Stage modal = createSaveModal(main);
        modal.show();
    }

    public void initModalCancelButtonClickEvent(Stage modal, Button cancelButton) {
        cancelButton.setOnAction(e -> modal.close());
    }
}
