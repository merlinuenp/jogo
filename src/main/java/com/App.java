package com;

import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.List;
import static javafx.application.Application.launch;

/**
 * Primeira tela.
 */
public class App extends Application {

    private Pane tela = new Pane();
    private double t = 0;   // controla o tempo entre os tiros do inimigo
    private Bloco jogador;
    

    private Parent criarConteudo() {
        System.out.println("criar conteudo...");
        
        tela.setPrefSize(600, 600);
        
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                atualizarTela();
            }
        };
        timer.start();

        // cria personagens 
        jogador = new Bloco(300, 550, 40, 40, "jogador", Color.BLUE);
        tela.getChildren().add(jogador);
        for (int i = 0; i < 5; i++) {
            Bloco p = new Bloco(90 + i * 100, 150, 30, 30, "inimigo", Color.RED);
            tela.getChildren().add(p);
        }

        return tela;
    }

    private void criaPersonagens() {
        
    }

    // Versão convencional
    // Coleta os personagens para atualizar a tela
    private List<Bloco> personagens() {
        System.out.println("personagens...");
        List<Bloco> lista = new ArrayList();
        for (Object o : tela.getChildren()) {
            lista.add((Bloco) o);
        }
        return lista;
    }

    // Versão funcional
//    private List<Personagem> personagens() {
//        return tela.getChildren().stream().map(n -> (Bloco) n).collect(Collectors.toList());
//    }
    private void atualizarTela() {
        System.out.println("atualizou tela...");
        t += 0.016;

        personagens().forEach(s -> {
            switch (s.getTipo()) {

                case "tiroinimigo":
                    s.moverParaBaixo();

                    if (s.getBoundsInParent().intersects(jogador.getBoundsInParent())) {
                        jogador.setMorto(true);
                        s.setMorto(true);
                    }
                    break;

                case "tirojogador":
                    s.moverParaCima();

                    personagens().stream().filter(e -> e.getTipo().equals("inimigo")).forEach(inimigo -> {
                        if (s.getBoundsInParent().intersects(inimigo.getBoundsInParent())) {
                            inimigo.setMorto(true);
                            s.setMorto(true);
                        }
                    });

                    break;

                case "inimigo":
                    if (t > 2) {
                        if (Math.random() < 0.3) {
                            atirar(s);
                        }
                    }
                    break;
            }
        });

        tela.getChildren().removeIf(n -> {
            Bloco s = (Bloco) n;
            return s.isMorto();
        });

        if (t > 2) {
            t = 0;
        }

    }

    private void atirar(Bloco quem) {
        Bloco bloco = new Bloco((int) quem.getTranslateX() + 20,
                (int) quem.getTranslateY(), 5, 20, "tiro" + quem.getTipo(), Color.BLACK);

        tela.getChildren().add(bloco);
    }

    @Override
    public void start(Stage palco) throws Exception {
        Scene cena = new Scene(criarConteudo());

        cena.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    jogador.moverParaEsquerda();
                    break;
                case D:
                    jogador.moverParaDireita();
                    break;
                case SPACE:
                    atirar(jogador);
                    break;
            }
        });

        palco.setScene(cena);
        palco.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
