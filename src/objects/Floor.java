package objects;

public class Floor extends Objects {

	public Floor(int x, int y) {
		super(x, y);
		setPosition(x, y);
	}

	@Override
	public String getName() {
		return "Floor";
	}

}
