package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.robot.Robot;

/**
 * Primeira tela.
 */
public class App extends Application {

    private final Pane tela = new Pane();
    private double t = 0;   // controla o tempo entre os tiros do inimigo
    private Bloco jogador;
    private Bloco figura;
    Robot robot = new Robot();

    private Parent criarCena() throws FileNotFoundException {
        tela.setPrefSize(600, 600);
        
        FileInputStream inputstream = new FileInputStream("C:\\Users\\User\\Downloads\\cafe.jpg");
        Image image = new Image(inputstream);
        ImageInput imagem = new ImageInput();
        imagem.setX(0.0);
        imagem.setY(0.0);
        imagem.setSource(image);
        figura = new Bloco(0, 0, 40, 40, "figura", Color.WHITE);
        figura.setEffect(imagem);
        tela.getChildren().add(figura);

        // cria personagens 
        jogador = new Bloco(300, 550, 40, 40, "jogador", Color.BLUE);
        tela.getChildren().add(jogador);
        for (int i = 0; i < 5; i++) {
            Bloco p = new Bloco(90 + i * 100, 150, 30, 30, "inimigo", Color.RED);
            tela.getChildren().add(p);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                atualizarTela();
            }
        };
        timer.start();
        return tela;
    }

    // Versão convencional
    // Coleta os personagens para atualizar a tela
    private List<Bloco> personagens() {
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
        t += 0.016;

        personagens().forEach(personagem -> {
            switch (personagem.getTipo()) {

                case "tiroinimigo":
                    personagem.moverParaBaixo();

                    if (personagem.getBoundsInParent().intersects(jogador.getBoundsInParent())) {
                        jogador.setMorto(true);
                        personagem.setMorto(true);
                        tela.getChildren().remove(jogador);
                    }
                    break;

                case "tirojogador":
                    personagem.moverParaCima();

                    personagens().stream().filter(e -> e.getTipo().equals("inimigo")).forEach(inimigo -> {
                        if (personagem.getBoundsInParent().intersects(inimigo.getBoundsInParent())) {
                            inimigo.setMorto(true);
                            personagem.setMorto(true);
                        }
                    });

                    break;

                case "inimigo":
                    if (t > 2) {
                        if (Math.random() < 0.3) {
                            atirar(personagem);
                        }
                    }
                    break;

                case "figura":
                    figura.setTranslateX(robot.getMouseX());
                    figura.setTranslateY(robot.getMouseY());
                    break;
            }
        });
             
//        tela.getChildren().removeIf(n -> {
//            Bloco s = (Bloco) n;
//            s.isMorto();
//        });

        if (t > 2) {
            t = 0;
        }

    }

    private void atirar(Bloco quemAtirou) {
        if (quemAtirou.isMorto()){
            return; 
        }
        
        Bloco bloco = new Bloco((int) quemAtirou.getTranslateX() + 20,
                (int) quemAtirou.getTranslateY(), 5, 20, "tiro" + quemAtirou.getTipo(), Color.BLACK);

        tela.getChildren().add(bloco);  //adiciona um tiro 
    }

    @Override
    public void start(Stage palco) throws Exception {
        Scene cena = new Scene(criarCena());

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
