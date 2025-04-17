package objects;

public class Stair extends Objects implements Interagivel_Porta_Escada {
    public Stair(int x, int y) {
        super(x,y);
    }

    @Override
    public String getName() {
        return "Stairs";
    }

    @Override
    public int getLayer() {
        return SECOND_LAYER;
    }

    @Override
    public void interage(Toy toy) {

    }
}
