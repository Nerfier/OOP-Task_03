package se.adlez.game;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    public int getX() {return x;}
    public int getY() {return y;}
    
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}

    @Override
    public boolean equals(Object o) {
        Position p = (Position) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return String.format("x: %d | y: %d", x, y);
    }

    public void move(Position relative) {
        // TODO
    }
}
