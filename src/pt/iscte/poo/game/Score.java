package pt.iscte.poo.game;

public class Score  {

    private String nome;
    private int ticksFinais;

    public Score(String nome, int ticksFinais) {
        this.nome = nome;
        this.ticksFinais = ticksFinais;
    }

    public String getNome() {
        return nome;
    }

    public int getTicksFinais() {
        return ticksFinais;
    }

    @Override
    public String toString() {
        return "Score{" +
                "nome='" + nome + '\'' +
                ", ticksFinais=" + ticksFinais +
                '}';
    }
}
