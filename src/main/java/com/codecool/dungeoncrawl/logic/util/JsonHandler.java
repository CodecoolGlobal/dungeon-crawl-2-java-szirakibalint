package com.codecool.dungeoncrawl.logic.util;

import com.codecool.dungeoncrawl.Main;
import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonHandler {
    public void importGame() {
        File selectedFile = new FileChooser().showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getName());
            try {
                Gson gson = getGson();
                JsonReader reader = new JsonReader(new FileReader(selectedFile));

                GameMap map = gson.fromJson(reader, GameMap.class);
                setRemainingAttributes(map);
                Main.setMap(map);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else {
            System.out.println("File selection cancelled.");
        }
    }

    public void setRemainingAttributes(GameMap map) {
        for (Cell[] row : map.getCells()){
            for (Cell cell: row) {
                cell.setGameMap(map);
                Actor actor = cell.getActor();
                if (actor != null) {
                    actor.setCell(cell);
                    if (actor instanceof Player){
                        map.setPlayer((Player) actor);
                    }
                }
            }
        }
    }

    private Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Item.class,
                        new ItemSerializer())
                .registerTypeAdapter(Actor.class,
                        new ItemSerializer()).create();
    }

    public void exportGame(GameMap map) {
        File selectedFile = new FileChooser().showSaveDialog(null);

        if (selectedFile != null) {
            System.out.println("File selected: " + selectedFile.getName());
            try {
                Gson gson = getGson();
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
    }

}
