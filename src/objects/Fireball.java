package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Direction;

public class Fireball extends Objects implements CoisasQueMexem_Banana_Martelo {
        private static final int ROOM_LENGHT = 9;


    public Fireball(int x, int y) {
            super(x, y);
        }

        public void move() {

            if (position.getY() < ROOM_LENGHT) {
                position = position.plus(Direction.RIGHT.asVector());
            } else {
                Room.roomScan.remove(this);
                ImageGUI.getInstance().removeImage(this);
            }
        }

        @Override
        public String getName() {
            return "Fire";
        }

        @Override
        public int getLayer() {
            return SECOND_LAYER;
        }
}

