package se.adlez.game;

import java.util.HashMap;
import java.util.Map;

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
        map = new HashMap<Position,Item>();

        // Create the string
        String s = "";
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {
                s += "🟩";
            }
            s += "\n";
        }
    }

    public String getGamePlan() {
        String s = "";
        
        for(int i = HEIGHT; i > 0; i--) {
            for(int j = 0; j < WIDTH; j++) {
                s += " x";
            }
            s += "\n";
        }
        

        for(Position pos : map.keySet()) {
            int x = pos.getX();
            int y = pos.getY();

            int l = (y * 21 + (21 - 2*x));
            System.out.println(l);
            String n = "";
            n = s.substring(0, s.length() - l);
            n += " " + map.get(pos).getGraphic().substring(0, 1);
            n += s.substring((s.length() - l + 2), s.length());
            //n = s.substring(0, 15) + map.get(pos).getGraphic() + s.substring(16, s.length());
            s = n;
        }

        s = s.replaceAll("x", "🟩");

        return s;
    }

    public void addItem(Item item, Position position) {
        map.put(position, item);
        // TODO error handling coordinates
    }

    public String listItems() {
        String s = "";
        for(Position pos : map.keySet()) {
            s += String.format("(%d, %d) %s\n", pos.getX(), pos.getY(), map.get(pos).toString());
        }
        return s;
    }

    public boolean tryAddItem() {
        // TODO
        return false;
    }

    public void addPlayerItem(AbstractMoveableItem player) {
        // TODO
    }

    public void addHunterItem(AbstractMoveableItem hunter) {
        // TODO
    }

    public void addHomeItem(AbstractMoveableItem home) {
        // TODO
    }

    public void movePlayer(Position relative) {
        // TODO
    }

    public boolean isGameOver() {return gameOver;}

    public String getStatus() {return status.toString();}
}
