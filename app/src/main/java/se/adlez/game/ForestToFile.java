package se.adlez.game;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Scanner;

public class ForestToFile {

    Scanner scanner = new Scanner(System.in);
    ObjectInputStream in;
    ObjectOutputStream out;

    public ForestToFile() {
        
    }

    public void save(Forest forest, String fileName) {
        try {
            out = new ObjectOutputStream(
                    new BufferedOutputStream(
                        new FileOutputStream(fileName)
                )
            );
            
            out.writeObject(forest);
            out.flush();
            out.close();
            System.out.println("Saved serialized forest to file '" + fileName + "'.");
        } catch (IOException e) {
            System.out.println(e);
        } 
    }

    public Forest load(String fileName) {
        Forest f = null;
        try {
            in = new ObjectInputStream(
                new BufferedInputStream(
                    new FileInputStream(fileName)
                )
            );
            f = (Forest) in.readObject();
            in.close();
            System.out.println("Loaded serialized forest from file '" + fileName + "'.");
            return f;
        } catch (Exception e) {
            System.out.println("Loading a new Forest due to an error");    
        }
        return new Forest();
    }


    public String toJson(Forest forest) {
        return ForestToJson.toJson(forest);
    }

    public void saveAsJson(Forest forest, String filename) {
        ForestToJson.saveAsJson(forest, filename);
    }  
}
