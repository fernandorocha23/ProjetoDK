package objects;

import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.gui.ImageTile;
import pt.iscte.poo.utils.Direction;
import pt.iscte.poo.utils.Point2D;

import java.util.ArrayList;
import java.util.List;

import static pt.iscte.poo.game.Room.*;

public class Kong extends Objects implements Viloes, Damage, Obstacles {

    private int vidaKong = 100;

    private static Kong instance;
    private Point2D position;

    private static final int ROOM_LEN = 9;
//    private int timeDelay = 0;
//    private static final int INTERVALO = Integer.parseInt(ImageGUI.getInstance().askUser(" Escolha o Nível de Dificuldade (Fácil-2, Médio-1, Díficil-0)"));

    public List<Banana> bananas = new ArrayList<>();

    private Kong(int x, int y) {
        super(x, y);
        this.position = new Point2D(x, y);
        vilaoScan.add(this);
        obstaclesList.add(this);

    }

    public static Kong getInstance() {
        if (instance == null)
            instance = new Kong(8,0);
        return instance;
    }

    @Override
    public void move() {
        if(vidaKong > 0) {
//            if (timeDelay < INTERVALO) {
//                timeDelay++;
//                return;
//            }
//            timeDelay = 0;
            Direction direcao = (Math.random() <= 0.5) ? Direction.LEFT : Direction.RIGHT;
            Point2D novaPosicao = position.plus(direcao.asVector());

            if (novaPosicao.getX() < 1 || novaPosicao.getX() > ROOM_LEN)
                return;
            if (!verificarCombate(direcao) && !collidesWithObstacle(novaPosicao) && !collidesWithHeroi(novaPosicao)) {
                position = novaPosicao;
            }
            atirarBanana();
        }
    }

    public boolean verificarCombate(Direction d) {
        Toy toy = Toy.getInstance();
        for (ImageTile tile : roomScan)
            if (tile instanceof Heroi && position.plus(d.asVector()).equals(toy.getPosition()))
                if (!toy.temEscudo()) {
                    System.out.println("Kong atacou Toy! Causou 15 de dano em Toy!");
                    toy.recebeDano(15);
                    return true;
                }
        return false;
    }

    private boolean collidesWithObstacle(Point2D insideBounds) {
        for (ImageTile pixel : roomScan)
            if (pixel instanceof Obstacles && pixel.getPosition().equals(insideBounds))
                return true;
        return false;
    }

    private boolean collidesWithHeroi(Point2D insideBounds) {
        for (ImageTile pixel : roomScan)
            if (pixel instanceof Heroi && pixel.getPosition().equals(insideBounds))
                return true;
        return false;
    }

    private void atirarBanana() {
        if(vidaKong  > 0) {
            Banana banana = new Banana(position.getX(), position.getY());
            bananas.add(banana);
            ImageGUI.getInstance().addImage(banana);
        }
    }

    public void tocouNoMarteloOuBola() {
        List<CoisasQueMexem_Banana_Martelo> marteloOuBola = Toy.getInstance().coisasQueMexem;

        for (CoisasQueMexem_Banana_Martelo coisasQueMexem : marteloOuBola) {
            if (coisasQueMexem.getPosition().equals(position)){
                recebeDano(100);
                marteloOuBola.remove(coisasQueMexem);
                ImageGUI.getInstance().removeImage(coisasQueMexem);
                return;
            }
        }
    }


    @Override
    public String getName() {
        return "DonkeyKong";
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

    public int getVidaKong() {
        return vidaKong;
    }

    @Override
    public void recebeDano(int dano) {
        vidaKong -= dano;
        if (vidaKong > 0 ) {
            System.out.println("Vida do Kong: " + vidaKong);
            atualizarStatus();
        } else {
            vidaKong = 0;
            kongMorreu();
        }
    }

    public void kongMorreu() {
        System.out.println("Kong morreu");
        ImageGUI.getInstance().setStatusMessage("Kong morreu");


        for (ImageTile tile : roomScan) {
            if (tile instanceof Door) {
                Door door = (Door) tile;
                door.open();
                return;
            }
        }
    }

    private void atualizarStatus(){
        Toy toy = Toy.getInstance();
        toy.atualizarStatus();
    }

    public void setVida(int vida){
        this.vidaKong = vida;
    }

    @Override
    public boolean eUmObstaculo() {
        return true;
    }
}