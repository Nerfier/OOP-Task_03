package se.adlez.game;

public abstract class AbstractMoveableItem extends AbstractItem implements Moveable {
    private Position position;

    public AbstractMoveableItem(String description, String graphic, Position position) {
        super(description, graphic);
        this.position = position;
    }

    @Override
    public String toString() {
        // TODO
        return "";
    }

    public Position getPosition() {return position;}
}
