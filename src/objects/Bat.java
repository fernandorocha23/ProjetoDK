package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

import static pt.iscte.poo.game.Room.*;

public class Bat extends Objects implements Viloes, Damage, Obstacles{

    private static final int ROOM_LEN = 9;
    private int vida = 50;

    public Bat(int x, int y) {
        super(x, y);
        obstaclesList.add(this);
    }

    @Override
    public String getName() {
        return "Bat";
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
        return THIRD_LAYER;
    }

    @Override
    public void move() {

        if (Kong.getInstance().getVidaKong() == 0) {
            ImageGUI.getInstance().removeImage(this);
            return;

        }

        Direction direcao = (Math.random() < 0.5) ? Direction.LEFT : Direction.RIGHT;
        Direction vaiParaBaixo = Direction.DOWN;
        Point2D novaPosicao = position.plus(direcao.asVector());
        Point2D desceEscada = position.plus(vaiParaBaixo.asVector());

        if (encontrouEscada())
            position = desceEscada;

        if (novaPosicao.getX() < 1 || novaPosicao.getX() > ROOM_LEN)
            return;

        if (!verificarCombate(direcao) && !encontrouEscada() &&!collidesWithObstacle(novaPosicao) && !collidesWithHeroi(novaPosicao)) {
            position = novaPosicao;


        }
    }

    private boolean encontrouEscada(){
        for (ImageTile escada : roomScan)
            if (escada instanceof Stair && escada.getPosition().equals(new Point2D(position.getX(), position.getY() + 1))) {
                return true;
            }
        return false;
    }

    private boolean collidesWithHeroi(Point2D insideBounds) {
        for (ImageTile pixel : roomScan)
            if (pixel instanceof Heroi && pixel.getPosition().equals(insideBounds))
                return true;
        return false;
    }

    private boolean collidesWithObstacle(Point2D position) {
        for (ImageTile pixel : roomScan)
            if (pixel instanceof Obstacles && pixel.getPosition().equals(position))
                return true;
        return false;
    }

    public boolean verificarCombate(Direction d) {
        Toy toy = Toy.getInstance();
        for (ImageTile tile : roomScan)
            if (tile instanceof Heroi && position.plus(d.asVector()).equals(toy.getPosition())) {
                if (!toy.temEscudo()) {
                    System.out.println("Bat atacou Toy! Causou 15 de dano em Toy!");
                    toy.recebeDano(15);
                }
                return true;
            }
        return false;
    }

    @Override
    public void recebeDano(int dano) {
        vida -= dano;
        if (vida < 0 )
            morreu();
    }

    private void morreu() {
        ImageGUI.getInstance().removeImage(this);
        vilaoScan.remove(this);
        obstaclesList.remove(this);
        roomScan.remove(this);
    }

    @Override
    public boolean eUmObstaculo() {
        return true;
    }
}
