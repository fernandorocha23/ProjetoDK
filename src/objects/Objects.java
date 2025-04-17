package objects;

import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Point2D;

public abstract class Objects implements ImageTile {
    private static final int VALOR_ZERO = 0;
    public static final int FIRST_LAYER = 1;
    public static final int SECOND_LAYER = 2;
    public static final int THIRD_LAYER = 3;

    protected Point2D position;

    public Objects(int x, int y){
        this.position = new Point2D(x,y);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    @Override
    public void setPosition(int x, int y) {
        position = new Point2D(x,y);
    }

    @Override
    public int getLayer() {
        return VALOR_ZERO;
    }
}
