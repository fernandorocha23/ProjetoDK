package pt.iscte.poo.game;

import objects.*;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.observer.Observed;
import pt.iscte.poo.observer.Observer;
import pt.iscte.poo.utils.Direction;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static pt.iscte.poo.game.Room.*;

public class GameEngine implements Observer {

	private boolean gameOver = false;

	private Room currentRoom = new Room();
	private int lastTickProcessed = 0;

	Kong kong = Kong.getInstance();
	Toy toy = Toy.getInstance();
	ImageGUI gui = ImageGUI.getInstance();
	List<Viloes> remocao = new ArrayList<>();

	private ScoreBoard scoreBoard = new ScoreBoard();

	public GameEngine() { gui.update(); }

	@Override
	public void update(Observed source) {


		if (gui.wasKeyPressed()) {
			int k = gui.keyPressed();
			//System.out.println("Keypressed " + k);
			if( k == 'M') {
//				System.out.println("MMM");
				toy.atirarMartelo();
			}

			if (k == KeyEvent.VK_P) {
				toy.atirarBolaDeFogo();
			}

			//System.out.println("Keypressed " + k);
			if( k == 'M')
				System.out.println("MMM");

			if (Direction.isDirection(k)) {
				//System.out.println("Direction! ");
				currentRoom.moveToy(Direction.directionFor(k));
			}
		}

		if (gui.getTicks() > lastTickProcessed) {
//			System.out.println("Ticks: " + ImageGUI.getInstance().getTicks());
			vilaoScan.iterator().forEachRemaining(Viloes::move);
			lastTickProcessed = ImageGUI.getInstance().getTicks();
			toy.tocouNaBanana();

			kong.tocouNoMarteloOuBola();

			for (Consumivel_Espada_Bife_Escudo temp : Room.consumiveis)
				if (temp instanceof Temporario)
					((Temporario) temp).apodrecer();

			for (Consumivel_Espada_Bife_Escudo temp : Room.consumidos)
				if (temp instanceof Temporario)
					((Temporario) temp).apodrecer();

			if (kong.getVidaKong() == 0 && !gameOver) {
				gameOver = true;
				int ticksFinais = gui.getTicks();
				System.out.println("Concluíste o jogo em " + ticksFinais + " Ticks");

				String nomeJogador = gui.askUser("Como é que te chamas?");

				scoreBoard.adicionarScore(nomeJogador, ticksFinais);
				scoreBoard.escreverTopScores("topscores.txt", ticksFinais);

				remocao.add(kong);

			}

		}

		for (Banana banana : Kong.getInstance().bananas) {
			banana.move();
		}

		for (CoisasQueMexem_Banana_Martelo marteloEBola : Toy.getInstance().coisasQueMexem) {
			marteloEBola.move();
		}

		vilaoScan.removeAll(remocao);

		gui.removeImages(remocao);

		ImageGUI.getInstance().update();
	}

}
