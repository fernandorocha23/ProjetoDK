package objects;


import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;

public class Sword extends Objects implements Consumivel_Espada_Bife_Escudo {
    public Sword(int x, int y) {
        super(x, y);
    }

    @Override
    public String getName() {
        return "Sword";
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }

    @Override
    public void consumir(Toy toy) {
        toy.obterEspada();

        Room.consumiveis.remove(this);
        Room.consumidos.add(this);

        Room.roomScan.remove(this);
        ImageGUI.getInstance().removeImage(this);
    }
}
