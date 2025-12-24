package se.adlez.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    

    // TODO Alternate skins



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
        moveHunter(playerPos, hunterPos);
        if(hunterPos.equals(playerPos)) {
            status.append("Ohh no! The hunter captured the player!");
            gameOver = true;
        }
    }

// TODO Smarter Hunter

    /*  Hunter Behaviour:
            If the player is in sight (distance of 5 or less)
            Move directly toward the player
            Else
            Wander around the map
     */
    private void moveHunter(Position playerPos, Position hunterPos) {
        Position newPos = new Position(hunterPos);
        Position relative = new Position(0, 0);

        Random rand = new Random();

        if(!playerInSight(playerPos, hunterPos)) { // If not in sight wander randomly around
            do {
                int a = rand.nextInt(4); // Pick a random direction
                switch (a) {
                    case 0:
                        relative = new Position(0, 1); break;
                    case 1:
                        relative = new Position(-1,0); break;
                    case 2:
                        relative = new Position(0, -1); break;
                    case 3:
                        relative = new Position(1, 0); break;
                }
                newPos.move(relative); // Move to that direction
            } while(!tryHunterMove(relative)); // Retry if move unsuccessful
            return;
        }


        // Targeting the player
        int playerX = playerPos.getX(); int playerY = playerPos.getY();
        int hunterX = hunterPos.getX(); int hunterY = hunterPos.getY();
        int diffX = hunterX == playerX ? 0 : hunterX - playerX > 0 ? -1 : 1; // 0 if on the same lvl; 1 if X of the player is greater; else -1
        int diffY = hunterY == playerY ? 0 : hunterY - playerY > 0 ? -1 : 1; // 0 if on the same lvl; 1 if Y of the player is greater; else -1
        
        // Try to get closer on the x axis
        if(diffX != 0) {
            if(tryHunterMove(new Position(diffX, 0))) {
                return;
            }
        }
        // Try to get closer on the y axis
        if(diffY != 0) {
            if(tryHunterMove(new Position(0, diffY))) {
                return;
            }
        }
    }

    private boolean playerInSight(Position playerPos, Position hunterPos) { // return if the player is 5 units or less away
        // Pythagora's theorem to get distance from hunter to player
        double distance = Math.sqrt(Math.pow(playerPos.getX() - hunterPos.getX(), 2) + Math.pow(hunterPos.getY() - playerPos.getY(), 2));
        return distance <= 4.0;
    }

    private boolean tryHunterMove(Position relative) { // Try to move the hunter; Return true if successful
        Position newPos = new Position(hunter.getPosition());
        newPos.move(relative);

        if(!isInBound(newPos) || (map.containsKey(newPos) && !newPos.equals(player.getPosition()))) { // Check if there is an item which is not the player or if newPos is out of bounds
            return false;
        }

        // Update position and map
        map.remove(hunter.getPosition());
        hunter.getPosition().move(relative);
        map.put(hunter.getPosition(), hunter);
        return true;
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
