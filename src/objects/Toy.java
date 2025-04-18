package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

import java.util.ArrayList;
import java.util.List;

import static pt.iscte.poo.game.Room.roomScan;
import static pt.iscte.poo.game.Room.vilaoScan;

public class Toy extends Objects implements Heroi, Health,Damage {

	private Shield escudo;

	private static final int ROOM_LEN = 9;
	private static final int ROOM_HEIGHT = 9;

	private int vidaToy;
	private Point2D position;

	private boolean hasShield = false;
	private boolean hasSword = false;

	private static Toy INSTANCE;

	public List<CoisasQueMexem_Banana_Martelo> coisasQueMexem = new ArrayList<>();


	private Toy(Point2D initialPosition){
        super(initialPosition.getX(), initialPosition.getY());
		this.vidaToy = 100;
	}

	public static Toy getInstance() {
		if (INSTANCE == null)
			INSTANCE = new Toy(new Point2D(1, 8));
		return INSTANCE;
	}
	
	@Override
	public String getName() {
		return "JumpMan";
	}

	@Override
	public Point2D getPosition() {
		return position;
	}

	@Override
	public void setPosition(int x, int y) {
		position = new Point2D(x, y);
	}

	@Override
	public int getLayer() {
		// TODO Auto-generated method stub
		return THIRD_LAYER;
	}

	public String getVidaToy() {
		return String.valueOf(vidaToy);
	}

	public void move(Direction d) {
		Point2D insideBounds = position.plus(d.asVector());

		if (insideBounds.getX() < 0 || insideBounds.getY() < 0 || insideBounds.getX() > ROOM_LEN || insideBounds.getY() > ROOM_HEIGHT)
			return;

		if (!collidesWithObstacle(insideBounds) && !verificarCombate(d))
			position = insideBounds;

		if (tocouNaArmadilha(position))
			recebeDano(10);
	}

	public void atirarMartelo() {

			Hammer martelo = new Hammer(position.getX(), position.getY());
			coisasQueMexem.add(martelo);
			ImageGUI.getInstance().addImage(martelo);
	}

	public void atirarBolaDeFogo() {

		Fireball bolaFogo = new Fireball(position.getX(), position.getY());
		coisasQueMexem.add(bolaFogo);
		ImageGUI.getInstance().addImage(bolaFogo);

	}


	private boolean tocouNaArmadilha(Point2D posicao) {
		for (ImageTile trap : roomScan)
			if (trap instanceof Trap && trap.getPosition().equals(new Point2D(posicao.getX(), posicao.getY() + 1))) {
				setPosition(1,8);
				return true;
			}
		return false;
	}

	private boolean collidesWithObstacle (Point2D insideBounds) {
		for(ImageTile pixel : roomScan)
			if (pixel instanceof Obstacles && pixel.getPosition().equals(insideBounds))
				return true;
		return false;
	}

	private boolean verificarCombate(Direction d){
			for (Viloes vilao : vilaoScan){
				if (vilao != null && vilao.getPosition().equals(position.plus(d.asVector()))) {
					if((hasSword)) {
						System.out.println("Toy atacou " + vilao.getName() + "! Causou 15 de dano!");
						vilao.recebeDano(10);
                    } else {
						System.out.println("Toy não tem espada e não pode atacar.");
                    }
                    return true;
                }
		}
			return false;
	}

	public void tocouNaBanana() {
		List<Banana> bananas = Kong.getInstance().bananas;

		for (int i = 0; i < bananas.size(); i++) {
			Banana banana = bananas.get(i);
			if (banana.getPosition().equals(position)) {
				recebeDano(5);
				bananas.remove(i);
				ImageGUI.getInstance().removeImage(banana);
				return;
			}
		}
	}

	public void obterEspada() {
		hasSword = true;
		atualizarStatus();
	}

	@Override
	public void recebeDano(int dano) {
		if (!hasShield) {
			vidaToy -= dano;

			if (vidaToy > 0) {
				atualizarStatus();
				System.out.println("Vida do Toy: " + vidaToy);
			} else {
				ImageGUI.getInstance().showMessage("Toy morreu.", "Tente Novamente");
				System.out.println("Toy morreu");

				vidaToy = 100;
				Kong.getInstance().setVida(100);
				setPosition(1, 8);
				atualizarStatus();
			}
		} 
	}

	public void atualizarStatus() {
		String status = " Vida do Toy: " + getVidaToy() + "    Vida do Kong: " + Kong.getInstance().getVidaKong();

		if (hasSword)
			status += "    Toy tem a espada! Pode atacar!";

		ImageGUI.getInstance().setStatusMessage(status);
	}

	@Override
	public void aumentarVida(int quantidade) {
		vidaToy += quantidade;
		atualizarStatus();
	}

	public boolean temEscudo(){
		return hasShield;
	}

	public void obteveEscudo(Shield escudo) {
			hasShield = true;
			this.escudo = escudo;
			escudo.obterEscudo();

	}

	public void perdeuEscudo(){
		hasShield = false;
		escudo.perdeuOEscudo();
		System.out.println("O tempo do escudo terminou!!");
	}



}
