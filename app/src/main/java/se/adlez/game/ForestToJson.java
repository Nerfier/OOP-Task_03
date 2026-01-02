package se.adlez.game;

import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ForestToJson {

    public static String toJson(Forest forest) {
        
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(forest);
    }

    public static void saveAsJson(Forest forest, String filename) {
        String json = toJson(forest);
        Path path = Path.of(filename);

        try {
            Files.writeString(path, json);
            System.out.println("Saved JSON data to '" + filename + "'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }  
}
