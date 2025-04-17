package objects;

public class Door extends Objects implements Interagivel_Porta_Escada {
    private boolean isClosed;
    private Toy toy;

    public Door(int x, int y) {
        super(x, y);
        isClosed = true;
    }

    public void open() {
        isClosed = false;
    }

    public void close() {
        isClosed = true;
    }

    @Override
    public String getName() {
        if (isClosed)
            return "DoorClosed";
        return "DoorOpen";
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }

    @Override
    public void interage(Toy toy) {

    }
}
