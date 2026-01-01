package se.adlez.game;

import java.io.Serializable;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

public class Forest implements Serializable{
    private final int WIDTH = 10;
    private final int HEIGHT = 10;

    private Map<Position, Item> map;

    private AbstractMoveableItem player;
    private AbstractMoveableItem hunter;
    private AbstractMoveableItem home;

    private boolean gameOver;
    private StringBuilder status;

    public Forest() {
        init();
    }

    public void init() {
        gameOver = false;
        status = new StringBuilder();
        map = new HashMap<Position,Item>();
    }

    public String getGamePlan() { // return the Gameplan as a string
        String[][] mapArr = new String[HEIGHT][WIDTH];
        StringBuilder s = new StringBuilder();

        // Make an empty grid
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {
                mapArr[i][j] = "🟩";
            }
        }
        
        // Add the items
        for(Position pos : map.keySet()) {
            mapArr[pos.getY()][pos.getX()] = map.get(pos).getGraphic();
        }

        // Convert to stringbuilder which can be outputted
        for(int i = HEIGHT - 1; i >= 0; i--) {
            for(int j = 0; j < WIDTH; j++) {
                String formatted = String.format("%-3s", mapArr[i][j]);
                s.append(formatted);
            }
            s.append("\n");
        }

        return s.toString();
    }

    public String listItems() {
        String s = "";
        if(map.keySet().size() == 0) {return "No items added yet";}

        for(Position pos : map.keySet()) {
            s += String.format("(%d, %d) %s\n", pos.getX(), pos.getY(), map.get(pos).toString());
        }

        return s;
    }






    public void addItem(Item item, Position position) {
        map.put(position, item);
    }

    public boolean tryAddItem(Item item, Position position) {
        if(map.containsKey(position)) {return false;}

        map.put(position, item);
        return true;
    }

    public void addPlayerItem(AbstractMoveableItem player) {
        this.player = player;
        map.put(this.player.getPosition(), this.player);
    }

    public void addHunterItem(AbstractMoveableItem hunter) {
        this.hunter = hunter;
        map.put(this.hunter.getPosition(), this.hunter);
    }

    public void addHomeItem(AbstractMoveableItem home) {
        this.home = home;
        map.put(this.home.getPosition(), this.home);
    }





    public void movePlayer(Position relative) {
        Position playerPos = player.getPosition();
        Position hunterPos = hunter.getPosition();
        Position newPos = new Position(playerPos);
        newPos.move(relative);

        if(newPos.equals(home.getPosition())) { // If new position is equal to the home the game is over
            gameOver = true;
            status.append("Player reached home!\n");
        }

        if(newPos.equals(hunter.getPosition())) { // if the player moves on the hunter the game is over
            gameOver = true;
            status.append("Oh no! The player moved on the hunter and was catched!\n");
        }
        
        if(isGameOver()) {status.append("The game is over!\n"); return;} // Return if the game is over

        if(!(map.containsKey(newPos) || !isInBound(newPos))) { // If there is an item or is out of bounds
            // Move the player, update map
            map.remove(playerPos);
            playerPos.move(relative);
            map.put(player.getPosition(), player);
            
            status.append("Player moved\n");
        } else {
            // Nothing happens, the player couldnt move there
            status.append("Player couldn't move\n");
        }

        // Hunter moves
        Position newHunterPos;
        try {
            ArrayList<Position> path = A_Star(hunterPos, playerPos);
            path.removeLast();
            newHunterPos = path.getLast();
        } catch (NoSuchElementException e) {
            newHunterPos = new Position(hunterPos);
        }
        
        Position hunterRelative = new Position(newHunterPos.getX() - hunterPos.getX(), newHunterPos.getY() - hunterPos.getY());
        map.remove(hunterPos);
        hunterPos.move(hunterRelative);
        map.put(hunter.getPosition(), hunter);
        if(newHunterPos.equals(player.getPosition())) {
            gameOver = true;
            status.append("Damn! The hunter caught the player!");
        }
    }

    private ArrayList<Position> reconstructPath(Map<Position, Position> cameFrom, Position current) {
        ArrayList<Position> path = new ArrayList<>();
        path.add(current);

        while(cameFrom.keySet().contains(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        return path;
    }

    private ArrayList<Position> A_Star(Position hunterPos, Position playerPos) {
        Position[] dir = {new Position(1, 0), 
                          new Position(-1, 0), 
                          new Position(0, 1), 
                          new Position(0, -1)};

        Set<Position> openSet = new HashSet<>();
        openSet.add(hunterPos);

        Map<Position, Position> cameFrom = new HashMap<>();

        Map<Position, Integer> gScore = new HashMap<>();
        gScore.put(hunterPos, 0);

        Map<Position, Integer> fScore = new HashMap<>();
        fScore.put(hunterPos, heuristics(hunterPos, playerPos));

        while(!openSet.isEmpty()) {
            int min = Integer.MAX_VALUE;
            Position minPos = null;
            for(Position pos : openSet) {
                if(fScore.getOrDefault(pos, Integer.MAX_VALUE) < min) {
                    min = fScore.get(pos);
                    minPos = pos;
                }
            }

            Position current = minPos;
            openSet.remove(current);

            if(current.equals(playerPos)) {
                return reconstructPath(cameFrom, current);
            }

            for(Position relative : dir) {
                Position newPos = new Position(current);
                newPos.move(relative);

                if(isInBound(newPos) && (!map.containsKey(newPos) || newPos.equals(playerPos))) {
                    int newScore = gScore.get(current) + 1;

                    if(newScore < gScore.getOrDefault(newPos, Integer.MAX_VALUE)) {
                        cameFrom.put(newPos, current);
                        gScore.put(newPos, newScore);
                        fScore.put(newPos, newScore + heuristics(newPos, playerPos));
                        
                        if(!openSet.contains(newPos)) {
                            openSet.add(newPos);
                        }

                    }
                }
                
            }

        }
        return new ArrayList<>();
    }

    private int heuristics(Position current, Position goal) {
        return Math.abs(current.getX() - goal.getX()) + Math.abs(current.getY() - goal.getY());
    }


    private boolean isInBound(Position pos) { // Return true only if the position is inside the game area
        int x = pos.getX(); int y = pos.getY();
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public boolean isGameOver() {return gameOver;}

    public String getStatus() {return status.toString();}
    public int getWIDTH() {return WIDTH;}
    public int getHEIGHT() {return HEIGHT;}
    public AbstractMoveableItem getPlayer() {return player;}
    public AbstractMoveableItem getHome() {return home;}
    public AbstractMoveableItem getHunter() {return hunter;}
}
