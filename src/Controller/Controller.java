package Controller;

import Model.Jogador;
import View.InserirNome;
import View.Tabuleiro;
import View.TabuleiroTiro;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {
    private Jogador jogador1;
    private Jogador jogador2;
    private Tabuleiro tabuleiro;
    public int currentPlayer;

    public Controller(Jogador jogador1, Jogador jogador2) {
        this.jogador1 = jogador1;
        this.jogador2 = jogador2;
        jogador1.addObserver(this);
        jogador2.addObserver(this);
        currentPlayer = 1;
    }

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }



    @Override
    public void update(Observable o, Object arg) {
        if (tabuleiro != null) {
            tabuleiro.repaint();
        }
    }

    public boolean inserirNavio(int currentPlayer, int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        if (currentPlayer == 1) {
            jogador1.salvarMatrizEmArquivo("matriznova1.txt");
            return jogador1.inserirNavio(tipoNavio, linhaInicial, colunaInicial, orientacao);
        } else {
            jogador2.salvarMatrizEmArquivo("matriznova2.txt");
            return jogador2.inserirNavio(tipoNavio, linhaInicial, colunaInicial, orientacao);
        }
    }
    public void resetJogadorTabuleiro(int currentPlayer) {
        if (currentPlayer == 1) {
            jogador1.resetTabuleiro();
        } else {
            jogador2.resetTabuleiro();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame panel = new JFrame();
            InserirNome gui = new InserirNome(panel);
            gui.setVisible(true);

            Jogador jogador1 = new Jogador();
            Jogador jogador2 = new Jogador();
            jogador1.nome = gui.nome1;
            jogador2.nome = gui.nome2;

            if (jogador1.nome != null && jogador2.nome != null) {
                Controller controller = new Controller(jogador1, jogador2);
                Tabuleiro tabuleiro = new Tabuleiro(jogador1.nome, jogador2.nome, controller);
                tabuleiro.setVisible(true);

                // Adicionar um WindowListener para detectar quando a janela de inserção for fechada
                tabuleiro.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        SwingUtilities.invokeLater(() -> {
                            TabuleiroTiro gui2 = new TabuleiroTiro(jogador1.nome, jogador2.nome);
                            gui2.setVisible(true);
                        });
                    }
                });
            } else {
                System.out.println("Nomes dos jogadores não inseridos corretamente.");
            }
        });

    }



}