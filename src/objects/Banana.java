package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;


public class Banana extends Objects implements CoisasQueMexem_Banana_Martelo {
    private static final int ROOM_HEIGHT = 9;


    public Banana(int x, int y) {
        super(x, y);
    }

    @Override
    public void move() {

        if (position.getY() < ROOM_HEIGHT) {
            position = position.plus(Direction.DOWN.asVector());
        } else {
            Room.roomScan.remove(this);
            ImageGUI.getInstance().removeImage(this);
        }
    }
    

    @Override
    public String getName() {
        return "Banana";
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }
}
