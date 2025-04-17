package objects;

import pt.iscte.poo.game.Room;
import pt.iscte.poo.gui.ImageGUI;
import pt.iscte.poo.utils.Point2D;

import static pt.iscte.poo.game.Room.consumiveis;

public class Shield extends Objects implements Consumivel_Espada_Bife_Escudo, Temporario {

    private int tempoAteAcabar;
    private boolean escudoAtivo = false;

    public Shield(int x, int y) {
        super(x, y);
        consumiveis.add(this);
        tempoAteAcabar = 10;
    }

    @Override
    public void consumir(Toy toy) {
        toy.obteveEscudo(this);

        consumiveis.remove(this);
        Room.consumidos.add(this);

        Room.roomScan.remove(this);
        ImageGUI.getInstance().removeImage(this);
    }

    @Override
    public void apodrecer() {
        if (escudoAtivo) {
            if (tempoAteAcabar > 0)
                tempoAteAcabar--;

            else {
                Toy.getInstance().perdeuEscudo();
            }
        }
    }

    public void obterEscudo(){
        escudoAtivo = true;
    }

    public void perdeuOEscudo(){
        escudoAtivo = false;
    }

    @Override
    public String getName() {
        return "Shield";
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
}
