package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import static java.awt.Color.WHITE;

public class TabuleiroTiro extends JFrame {
    private static final int SIZE = 15;  // Tamanho do tabuleiro
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula

    private String player1Name;
    private String player2Name;

    private boolean[][] player1Shots;
    private boolean[][] player2Shots;
    private int currentPlayer;

    public TabuleiroTiro(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        player1Shots = new boolean[SIZE][SIZE];
        player2Shots = new boolean[SIZE][SIZE];
        currentPlayer = 1; // Começa com o jogador 1

        setTitle("Batalha Naval - Tiros");
        setSize(SIZE * CELL_SIZE * 2 + 100, SIZE * CELL_SIZE + 70);  // Dimensões da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TabuleiroPanel player1Tabuleiro = new TabuleiroPanel(player1Shots, "Tabuleiro de " + player1Name);
        TabuleiroPanel player2Tabuleiro = new TabuleiroPanel(player2Shots, "Tabuleiro de " + player2Name);

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

        setLayout(new GridLayout(1, 2));
        add(player1Tabuleiro);
        add(player2Tabuleiro);
    }

    private class TabuleiroPanel extends JPanel {
        private boolean[][] shots;
        private String playerName;

        public TabuleiroPanel(boolean[][] shots, String playerName) {
            this.shots = shots;
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
                        g2d.setColor(Color.RED);
                        g2d.fill(cell);
                    }
                }
            }

            // Desenhar nome do jogador
            g2d.drawString(playerName, 10, 20);
        }
    }

    private void handleMouseClick(MouseEvent e, TabuleiroPanel tabuleiroPanel) {
        if ((currentPlayer == 1 && tabuleiroPanel == getComponent(1)) ||
                (currentPlayer == 2 && tabuleiroPanel == getComponent(0))) {
            int x = e.getX();
            int y = e.getY();

            int panelWidth = tabuleiroPanel.getWidth();
            int panelHeight = tabuleiroPanel.getHeight();
            int tabuleiroWidth = SIZE * CELL_SIZE;
            int tabuleiroHeight = SIZE * CELL_SIZE;

            int startX = (panelWidth - tabuleiroWidth) / 2;
            int startY = (panelHeight - tabuleiroHeight) / 2;

            int col = (x - startX) / CELL_SIZE;
            int row = (y - startY) / CELL_SIZE;

            if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
                if (currentPlayer == 1) {
                    player2Shots[row][col] = true;
                    currentPlayer = 2;
                } else {
                    player1Shots[row][col] = true;
                    currentPlayer = 1;
                }
                repaint();
            }
        }
    }
}
