package View;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

import static java.awt.Color.WHITE;

public class TabuleiroTiro extends JFrame {
    private static final int SIZE = 15;  // Tamanho do tabuleiro
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula

    private String player1Name;
    private String player2Name;

    private boolean[][] player1Shots;
    private boolean[][] player2Shots;
    private int[][] player1Embarcacoes;
    private int[][] player2Embarcacoes;
    private int currentPlayer;
    private int tirosRestantes;
    private static TabuleiroTiro instance;

    private TabuleiroPanel player1Tabuleiro;
    private TabuleiroPanel player2Tabuleiro;
    private JButton atirarButton;

    private int selectedRow = -1;
    private int selectedCol = -1;

    private Controller controller;

    public TabuleiroTiro(String player1Name, String player2Name, Controller controller) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.controller = controller;

        player1Shots = controller.getTiros(1);
        player2Shots = controller.getTiros(2);
        player1Embarcacoes = controller.getTabuleiro(1);
        player2Embarcacoes = controller.getTabuleiro(2);
        currentPlayer = 1; // Começa com o jogador 1
        tirosRestantes = 3; // Cada jogador tem 3 tiros por turno

        setTitle("Batalha Naval - Tiros");
        setSize(SIZE * CELL_SIZE * 2 + 100, SIZE * CELL_SIZE + 100);  // Dimensões da janela
        setUndecorated(true); // Remove a barra de título
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza a janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        player1Tabuleiro = new TabuleiroPanel(player1Shots, player1Embarcacoes, "Tabuleiro de " + player1Name);
        player2Tabuleiro = new TabuleiroPanel(player2Shots, player2Embarcacoes, "Tabuleiro de " + player2Name);

        player1Tabuleiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e, player1Tabuleiro);
            }
        });

        player2Tabuleiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e, player2Tabuleiro);
            }
        });

        atirarButton = new JButton("Atirar");
        atirarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleShootButton();
            }
        });

        setLayout(new BorderLayout());
        JPanel tabuleirosPanel = new JPanel(new GridLayout(1, 2));
        tabuleirosPanel.add(player1Tabuleiro);
        tabuleirosPanel.add(player2Tabuleiro);

        add(tabuleirosPanel, BorderLayout.CENTER);
        add(atirarButton, BorderLayout.SOUTH);
    }

    public static TabuleiroTiro getInstance(String player1Name, String player2Name, Controller controller) {
        if (instance == null) {
            instance = new TabuleiroTiro(player1Name,player2Name,controller);
        }
        return instance;
    }

    private class TabuleiroPanel extends JPanel {
        private boolean[][] shots;
        private int[][] embarcacoes;
        private String playerName;

        public TabuleiroPanel(boolean[][] shots, int[][] embarcacoes, String playerName) {
            this.shots = shots;
            this.embarcacoes = embarcacoes;
            this.playerName = playerName;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int tabuleiroWidth = SIZE * CELL_SIZE;
            int tabuleiroHeight = SIZE * CELL_SIZE;

            int startX = (panelWidth - tabuleiroWidth) / 2;
            int startY = (panelHeight - tabuleiroHeight) / 2;

            // Desenhar números na parte superior
            for (int j = 0; j < SIZE; j++) {
                String num = Integer.toString(j + 1);
                int numWidth = g2d.getFontMetrics().stringWidth(num);
                g2d.drawString(num, startX + j * CELL_SIZE + (CELL_SIZE - numWidth) / 2, startY - 5);
            }

            // Desenhar letras no lado esquerdo
            for (int i = 0; i < SIZE; i++) {
                char letter = (char) ('A' + i);
                int letterHeight = g2d.getFontMetrics().getAscent();
                g2d.drawString(Character.toString(letter), startX - 20, startY + i * CELL_SIZE + (CELL_SIZE + letterHeight) / 2);
            }

            // Desenhar células do tabuleiro
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Rectangle2D.Double cell = new Rectangle2D.Double(startX + j * CELL_SIZE, startY + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2d.setColor(WHITE);  // Cor do preenchimento da célula
                    g2d.fill(cell);  // Preencher a célula
                    g2d.setColor(Color.BLACK);  // Cor do contorno da célula
                    g2d.draw(cell);  // Desenhar o contorno da célula

                    if (shots[i][j]) {
                        if (embarcacoes[i][j] < 0) {//talvez >
                            if (embarcacoes[i][j] == -10) {
                                g2d.setColor(Color.BLUE); // Tiro na água
                                g2d.fill(cell);
                            }
                            else{
                                g2d.setColor(Color.RED); // Tiro que acertou uma embarcação
                                g2d.fill(cell);
                            }
                        }

                    } else if (selectedRow == i && selectedCol == j && this == player2Tabuleiro && currentPlayer == 1) {
                        g2d.setColor(Color.GRAY);
                        g2d.fill(cell);
                    } else if (selectedRow == i && selectedCol == j && this == player1Tabuleiro && currentPlayer == 2) {
                        g2d.setColor(Color.GRAY);
                        g2d.fill(cell);
                    }
                }
            }

            // Desenhar nome do jogador
            g2d.drawString(playerName, 10, 20);
        }
    }

    private void handleMouseClick(MouseEvent e, TabuleiroPanel tabuleiroPanel) {
        if ((currentPlayer == 1 && tabuleiroPanel == player2Tabuleiro) ||
                (currentPlayer == 2 && tabuleiroPanel == player1Tabuleiro)) { // Permite clique apenas no tabuleiro do oponente
            int x = e.getX();
            int y = e.getY();

            int[] coordenadas = converterCoordenada(x, y, tabuleiroPanel);
            if (coordenadas != null) {
                selectedRow = coordenadas[0];
                selectedCol = coordenadas[1];
                tabuleiroPanel.repaint();  // Repinta o painel para mostrar a seleção
                System.out.println("Selected cell: (" + selectedRow + ", " + selectedCol + ")");
            }
        }
    }

    private int[] converterCoordenada(int x, int y, TabuleiroPanel tabuleiroPanel) {
        int panelWidth = tabuleiroPanel.getWidth();
        int panelHeight = tabuleiroPanel.getHeight();
        int tabuleiroWidth = SIZE * CELL_SIZE;
        int tabuleiroHeight = SIZE * CELL_SIZE;

        int startX = (panelWidth - tabuleiroWidth) / 2;
        int startY = (panelHeight - tabuleiroHeight) / 2;

        int col = (x - startX) / CELL_SIZE;
        int row = (y - startY) / CELL_SIZE;

        if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
            return new int[]{row, col};
        }
        return null;
    }

    private void handleShootButton() {
        // Lógica para a ação do botão "Atirar"
        if (selectedRow != -1 && selectedCol != -1) {
            if (currentPlayer == 1 && !player2Shots[selectedRow][selectedCol]) { // Verifica se o tiro já foi feito
                int resultado = controller.registrarTiro(selectedRow, selectedCol, currentPlayer);
                if (resultado != -1) {
                    player2Shots[selectedRow][selectedCol] = true;
                    if (resultado > 0) { // Acertou uma embarcação
                        //player2Embarcacoes[selectedRow][selectedCol] = resultado; // Marca o tiro acertado
                    } else { // Acertou a água
                        //player2Embarcacoes[selectedRow][selectedCol] = -10; // Marca tiro na água
                    }
                    repaint();
                    if (checkVictory(player2Embarcacoes)) {
                        JOptionPane.showMessageDialog(this, player1Name + " venceu!");
                        System.exit(0);
                    }
                }

                tirosRestantes--;
                if (tirosRestantes == 0) {
                    currentPlayer = 2;
                    tirosRestantes = 3;
                    JOptionPane.showMessageDialog(this, "Vez do " + player2Name);
                }
                selectedRow = -1;
                selectedCol = -1;
                System.out.println("Atirou no quadrado selecionado.");
            } else if (currentPlayer == 2 && !player1Shots[selectedRow][selectedCol]) {
                int resultado = controller.registrarTiro(selectedRow, selectedCol, currentPlayer);
                if (resultado != -1) {
                    player1Shots[selectedRow][selectedCol] = true;
                    if (resultado > 0) { // Acertou uma embarcação
                        //player1Embarcacoes[selectedRow][selectedCol] = resultado; // Marca o tiro acertado
                    } else { // Acertou a água
                        //player1Embarcacoes[selectedRow][selectedCol] = -10; // Marca tiro na água
                    }
                    if (checkVictory(player1Embarcacoes)) {
                        JOptionPane.showMessageDialog(this, player2Name + " venceu!");
                        System.exit(0);
                    }
                    repaint();
                }
                tirosRestantes--;
                if (tirosRestantes == 0) {
                    currentPlayer = 1;
                    tirosRestantes = 3;
                    JOptionPane.showMessageDialog(this, "Vez do " + player1Name);
                }
                selectedRow = -1;
                selectedCol = -1;
                System.out.println("Atirou no quadrado selecionado.");
            } else {
                System.out.println("Este quadrado já foi alvo de um tiro.");
            }
        } else {
            System.out.println("Nenhum quadrado selecionado.");
        }
    }

    private boolean checkVictory(int[][] embarcacoes) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (embarcacoes[i][j] > 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
