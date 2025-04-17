package objects;

import pt.iscte.poo.game.Room;

public class Trap extends Objects implements Obstacles {
    public Trap(int x, int y){
        super(x,y);
        Room.obstaclesList.add(this);
    }

    @Override
    public String getName() {
        return "Trap";
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }

    @Override
    public boolean eUmObstaculo() {
        return true;
    }
}
