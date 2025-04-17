package pt.iscte.poo.game;

import objects.*;
import objects.Trap;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Room {
	
	private Point2D heroStartingPosition;
	private Toy toy;
	public static List<ImageTile> roomScan = new ArrayList<>();
	public static List<Viloes> vilaoScan = new ArrayList<>();
	public static List<Obstacles> obstaclesList = new ArrayList<>();
	public static List<Consumivel_Espada_Bife_Escudo> consumiveis = new ArrayList<>();
	public static List<Consumivel_Espada_Bife_Escudo> consumidos = new ArrayList<>();

	{

		try {
			Scanner fileScanner = new Scanner(new File("rooms\\room0.txt"));

			int y = 0;
			while (fileScanner.hasNextLine()) {
				String linha = fileScanner.nextLine();
				for (int x = 0; x < linha.length(); x++) {
					char c = linha.charAt(x);
					roomScan.add(new Floor(x, y));
					switch (c) {
						case 'W' : roomScan.add(new Wall(x, y));
						break;
						case 'S' : roomScan.add(new Stair(x, y));
						break;
						case '0' : roomScan.add(new Door(x, y));
						break;
						case 't' : roomScan.add(new Trap(x, y));
						break;
						case 's' : Sword sword = new Sword(x, y);
							roomScan.add(sword);
							consumiveis.add(sword);
						break;
						case 'E' : roomScan.add(new Shield(x, y));
						break;
						case 'M' : roomScan.add(new Meat(x, y));
						break;
						case 'H' : roomScan.add(new Princess(x, y));
						break;
						case 'J' : setHeroStartingPosition(x, y);
						break;
						case 'G' : Kong.getInstance().setPosition(x, y); vilaoScan.add(new Bat(x, y));
						break;
					}
				}
				y++;
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("Erro na abertura do ficheiro");
		}
	}

	public Room() {
		toy = Toy.getInstance();
		toy.setPosition(heroStartingPosition.getX(), heroStartingPosition.getY());
		roomScan.add(toy);
		ImageGUI.getInstance().addImages(roomScan);
		ImageGUI.getInstance().addImages(vilaoScan);
	}


	public void moveToy(Direction d) {
		toy.move(d);
		checkInteraction();
	}

	private void checkInteraction() {
		for (ImageTile tile : new ArrayList<>(roomScan)) {
			if (tile instanceof Consumivel_Espada_Bife_Escudo && tile.getPosition().equals(toy.getPosition())) {
				((Consumivel_Espada_Bife_Escudo) tile).consumir(toy);
			} else if (tile instanceof Interagivel_Porta_Escada && tile.getPosition().equals(toy.getPosition())) {
				((Interagivel_Porta_Escada) tile).interage(toy);
			}
		}
	}

	public void setHeroStartingPosition(int x, int y){
		heroStartingPosition = new Point2D(x, y);
	}
}