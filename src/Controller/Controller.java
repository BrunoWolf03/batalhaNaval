package Controller;

import Model.ModelAPI;
import View.InserirNome;
import View.Tabuleiro;
import View.TabuleiroTiro;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observer;

public class Controller implements Observer {
    private ModelAPI model;
    private Tabuleiro tabuleiro;
    public int currentPlayer;
    private static Controller instance;

    public Controller() {
        model = ModelAPI.getInstance();
        model.addObserver(this);
        currentPlayer = 1;
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    public void setTabuleiro(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if (tabuleiro != null) {
            tabuleiro.repaint();
        }
    }

    public boolean inserirNavio(int jogador, int tipoNavio, int linhaInicial, int colunaInicial, String orientacao) {
        model.salvarMatriz(jogador,"matriz.txt");
        return model.inserirNavio(jogador, tipoNavio, linhaInicial, colunaInicial, orientacao);
    }

    public void resetTabuleiro(int jogador) {
        model.resetTabuleiro(jogador);
    }

    public int registrarTiro(int linha, int coluna, int jogador) {
        return model.registrarTiro(linha, coluna, jogador);
    }

    public boolean[][] getTiros(int jogador) {
        return model.getTiros(jogador);
    }

    public int[][] getTabuleiro(int jogador) {
        return model.getTabuleiro(jogador);
    }

    public void salvarMatriz(int jogador, String fileName) {
        model.salvarMatriz(jogador, fileName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame panel = new JFrame();
            InserirNome gui = InserirNome.getInstance(panel);
            gui.setVisible(true);

            if (gui.nome1 != null && gui.nome2 != null) {
                ModelAPI.getInstance().setNome(1, gui.nome1);
                ModelAPI.getInstance().setNome(2, gui.nome2);

                Controller controller = getInstance();
                Tabuleiro tabuleiro = Tabuleiro.getInstance(gui.nome1, gui.nome2, controller);
                tabuleiro.setVisible(true);

                // Adicionar um WindowListener para detectar quando a janela de inserção for fechada
                tabuleiro.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        SwingUtilities.invokeLater(() -> {
                            TabuleiroTiro gui2 = TabuleiroTiro.getInstance(gui.nome1, gui.nome2, controller);
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
