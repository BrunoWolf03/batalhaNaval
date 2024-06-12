package Controller;

import Model.ModelAPI;
import View.InserirNome;
import View.Tabuleiro;
import View.TabuleiroTiro;
import View.TelaInicio;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

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
        if(jogador==1)
            model.salvarMatriz(jogador,"matriz1.txt");
        else
            model.salvarMatriz(jogador,"matriz2.txt");
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

    public void salvaroJogo(int[][] matriz1, int[][] matriz2) {

            model.salvarMatriz(1, "matriz1.txt");
            model.salvarMatriz(2, "matriz2.txt");
    }



    private static void processarArquivos(File file1, File file2,Controller controller, String nome1, String nome2) {
        // Adicione aqui a lógica para processar os arquivos

        TabuleiroTiro tabuleiroTiro = new TabuleiroTiro(nome1,nome2,controller);
        tabuleiroTiro.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // Criação da tela inicial
            TelaInicio telaInicio = new TelaInicio();
            telaInicio.setVisible(true);

            telaInicio.getStartButton().addActionListener(e -> { // Caso eu selecione a opção de Iniciar um novo jogo
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
            telaInicio.getLoadButton().addActionListener(e -> { // Caso eu selecione a opção de carregar
                Controller controller = getInstance();
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setMultiSelectionEnabled(true);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File[] selectedFiles = fileChooser.getSelectedFiles();
                    if (selectedFiles.length == 2) {
                        // Processar os arquivos e ir para a etapa de TabuleiroTiro
                        JFrame panel = new JFrame();
                        InserirNome gui = InserirNome.getInstance(panel);
                        gui.setVisible(true);
                        processarArquivos(selectedFiles[0], selectedFiles[1], controller, gui.nome1, gui.nome2);
                    } else {
                        JOptionPane.showMessageDialog(null, "Por favor, selecione exatamente dois arquivos.");
                    }
                }
            });
        });
    }
}

