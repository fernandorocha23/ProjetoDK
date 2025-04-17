package objects;

import static pt.iscte.poo.game.Room.obstaclesList;

public class Princess extends Objects implements Obstacles {
    public Princess(int x, int y) {
        super(x, y);
        obstaclesList.add(this);
    }

    @Override
    public String getName() {
        return "Princess";
    }

    @Override
    public int getLayer() {
        return THIRD_LAYER;
    }

    @Override
    public boolean eUmObstaculo() {
        return true;
    }
}
