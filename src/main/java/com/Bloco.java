package com;

import javafx.scene.paint.Color;  
import javafx.scene.shape.Rectangle;

public class Bloco extends Rectangle {
    private boolean morto; 
    private String tipo;    // se jogador ou inimigo 

    public Bloco(int posX, int posY, 
            int largura, int altura, 
            String tipo, Color color) {
        super(largura, altura, color);
        this.tipo = tipo;
        this.morto = false; 
        setTranslateX(posX);
        setTranslateY(posY);
    }

    void moverParaEsquerda() {
        double xAtual = getTranslateX();
        setTranslateX( xAtual - 5);
    }

    void moverParaDireita() {
        setTranslateX(getTranslateX() + 5);
    }

    void moverParaCima() {
        double yAtual = getTranslateY();
        setTranslateY(yAtual - 5);
    }

    void moverParaBaixo() {
        double yAtual = getTranslateY();
        setTranslateY(yAtual + 5);
    }

    public boolean isMorto() {
        return morto;
    }

    public void setMorto(boolean morto) {
        this.morto = morto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}

