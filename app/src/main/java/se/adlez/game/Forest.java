package se.adlez.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Forest {
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
        for(Position pos : map.keySet()) {
            s += String.format("(%d, %d) %s\n", pos.getX(), pos.getY(), map.get(pos).toString());
        }
        return s;
    }






    public void addItem(Item item, Position position) {
        map.put(position, item);
        // TODO error handling coordinates
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
            player.getPosition().move(relative);
            map.remove(playerPos);
            map.put(player.getPosition(), player);
            
            status.append("Player moved\n");
        } else {
            status.append("Player couldn't move\n");
            return;
        }

        /*  Hunter Behaviour:
            If the player is in sight (distance of 5 or less)
            Move directly toward the player
            Else
            Wander around the map
         */

        // TODO Wolf geht in die starter ecke??
        map.remove(hunter.getPosition());
        map.put(nextHunterMove(playerPos, hunterPos), hunter);

    }




    private Position nextHunterMove(Position playerPos, Position hunterPos) {
        Position newPos;
        Position relative = new Position(0, 0);

        if(!playerInSight(playerPos, hunterPos)) { // If not in sight wander randomly around
            Random rand = new Random();
            do {
                int a = rand.nextInt(4); // Pick a random direction
                switch (a) {
                    case 1:
                        relative = new Position(0, 1); break;
                    case 2:
                        relative = new Position(-1,0); break;
                    case 3:
                        relative = new Position(0, -1); break;
                    case 4:
                        relative = new Position(1, 0); break;
                }
                newPos = hunterPos;
                newPos.move(relative); // Move to that direction
            } while(!isInBound(newPos) || map.containsKey(newPos)); // While there is no obstacle or the move would be out of bounds
            return newPos;
        }

        int playerX = playerPos.getX(); int playerY = playerPos.getY();
        int hunterX = hunterPos.getX(); int hunterY = hunterPos.getY();

        //if(playerX > )

        // TODO wolf move on sight; x,y difference to zero? Dijkstra?

        return new Position(0, 0);
    }

    private boolean playerInSight(Position playerPos, Position hunterPos) { // return if the player is 5 units or less away
        // Pythagora's theorem to get distance from hunter to player
        double distance = Math.sqrt(Math.pow(playerPos.getX() - hunterPos.getX(), 2) + Math.pow(hunterPos.getY() - playerPos.getY(), 2));
        return distance <= 4.0;
    }

    private boolean tryHunterMove(Position relative) {
        Position newPos = hunter.getPosition();
        newPos.move(relative);

        if(map.containsKey(newPos) && !newPos.equals(player.getPosition())) {
            return false;
        }

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
}
