package pt.iscte.poo.game;

import objects.Toy;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

public class Main {

	public static void main(String[] args) {
		ImageGUI gui = ImageGUI.getInstance();
		GameEngine engine = new GameEngine(); // GameEngine engine = GameEngine.getInstance();
		gui.setStatusMessage(" Vida do Toy: 100    Vida do Kong: 100");
		gui.registerObserver(engine);
		gui.go();
	}
	
}
