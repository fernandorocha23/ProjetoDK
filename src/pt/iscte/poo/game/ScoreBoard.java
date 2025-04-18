package pt.iscte.poo.game;

import pt.iscte.poo.gui.ImageGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ScoreBoard {
    private List<Score> scores = new ArrayList<>();

    public void adicionarScore(String nome, int ticks){
        scores.add(new Score(nome, ticks));
    }

    public void lerOsScores (){
        try {
            Scanner fileScanner = new Scanner(new File("topscores.txt"));

            if(fileScanner.hasNextLine()) {
            fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {
                String linha = fileScanner.nextLine();

                if (linha.contains(":")) {

                    String[] partes = linha.split(":");

                    String nome = partes[0].trim();
                    String ticksString = partes[1].trim();

                    int ticks = Integer.parseInt(ticksString);
                    adicionarScore(nome, ticks);
                }

                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erro na abertura do ficheiro");
        }
    }

    public void escreverTopScores(String nome, int ticks) {
        lerOsScores();

        scores.sort((s1, s2) -> s1.getTicksFinais() - s2.getTicksFinais());
        while (scores.size() > 10)
            scores.removeLast();

        try (PrintWriter writer = new PrintWriter(new File(nome))){
            writer.println("Top scores: ");
            for (Score score : scores)
                writer.println(score.getNome() + ": " + score.getTicksFinais());
        } catch (FileNotFoundException e){
            System.err.println("Problema ao escrever o ficheiro");
        }

        int lugar = 0;
        for (Score s : scores) {
            lugar++;
            //ImageGUI.getInstance().showMessage("Parabéns "+ s.getNome() + "!",  lugar + "º lugar - Jogo concluído em " + s.getTicksFinais() + " ticks.");
            System.out.println(lugar + "º " + s.getNome() + " - " + s.getTicksFinais() + " ticks");

        }
    }
}
