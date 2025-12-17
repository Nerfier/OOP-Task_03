package se.adlez.game;

public abstract class AbstractMoveableItem extends AbstractItem implements Moveable {
    private Position position;

    public AbstractMoveableItem(String description, String graphic, Position position) {
        super(description, graphic);
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d %d)", getDescription(), getGraphic(), position.getX(), position.getY());
    }

    public Position getPosition() {return position;}
}
