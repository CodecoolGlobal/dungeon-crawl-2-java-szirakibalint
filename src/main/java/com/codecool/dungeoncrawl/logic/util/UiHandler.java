package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.Tiles;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

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
}
