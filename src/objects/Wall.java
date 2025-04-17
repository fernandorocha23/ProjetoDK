package objects;

import static pt.iscte.poo.game.Room.obstaclesList;

public class Wall extends Objects implements Obstacles {

	public Wall(int x, int y) {
		super(x, y);
		obstaclesList.add(this);
	}

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public int getLayer() {
		return FIRST_LAYER;
	}


	@Override
	public boolean eUmObstaculo() {
		return false;
	}
}
