package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;

public class Hammer extends Objects implements CoisasQueMexem_Banana_Martelo {
    private static final int ROOM_HEIGHT = 9;


    public Hammer(int x, int y) {
        super(x, y);
    }


    public void move() {

            if (position.getY() < ROOM_HEIGHT) {
                position = position.plus(Direction.UP.asVector());
            } else {
                Room.roomScan.remove(this);
                ImageGUI.getInstance().removeImage(this);
            }
    }

    @Override
    public String getName() {
        return "Hammer";
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }
}

