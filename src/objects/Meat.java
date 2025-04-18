package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

import static pt.iscte.poo.game.Room.consumiveis;

public class Meat extends Objects implements Consumivel_Espada_Bife_Escudo, Temporario{

    private int tempoAteApodrecer;

    public Meat(int x, int y) {
        super(x, y);
        consumiveis.add(this);
        tempoAteApodrecer = 15;
    }

    @Override
    public String getName() {
        if (!estaPodre())
            return "GoodMeat";
        else return "BadMeat";
    }

    public void apodrecer(){
        tempoAteApodrecer--;
    }

    public boolean estaPodre(){
        return tempoAteApodrecer < 0;
    }

    @Override
    public Point2D getPosition() {
        return super.getPosition();
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }

    @Override
    public void consumir(Toy toy) {

        if (!estaPodre()) {
            toy.aumentarVida(20);
            System.out.println("Toy comeu um bife bom e ganhou 20 de vida. Vida do Toy: " + toy.getVidaToy());
        }
        else {
            toy.recebeDano(20);
            System.out.println("Toy comeu um bife podre e perdeu 20 de vida. Vida do Toy: " + toy.getVidaToy());
        }

        consumiveis.remove(this);
        Room.consumidos.add(this);

        Room.roomScan.remove(this);
        ImageGUI.getInstance().removeImage(this);
    }


}
