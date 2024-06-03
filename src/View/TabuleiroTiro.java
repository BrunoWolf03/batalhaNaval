package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

import static java.awt.Color.WHITE;

public class TabuleiroTiro extends JFrame {
    private static final int SIZE = 15;  // Tamanho do tabuleiro
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula

    private String player1Name;
    private String player2Name;

    private boolean[][] player1Shots;
    private boolean[][] player2Shots;
    private boolean[][] player2Embarcacoes;
    private int currentPlayer;

    private TabuleiroPanel player1Tabuleiro;
    private TabuleiroPanel player2Tabuleiro;
    private JButton atirarButton;

    private int selectedRow = -1;
    private int selectedCol = -1;

    public TabuleiroTiro(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        player1Shots = new boolean[SIZE][SIZE];
        player2Shots = new boolean[SIZE][SIZE];
        player2Embarcacoes = new boolean[SIZE][SIZE]; // Adicionando embarcações do jogador 2
        currentPlayer = 1; // Começa com o jogador 1

        setTitle("Batalha Naval - Tiros");
        setSize(SIZE * CELL_SIZE * 2 + 100, SIZE * CELL_SIZE + 100);  // Dimensões da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        player1Tabuleiro = new TabuleiroPanel(player1Shots, "Tabuleiro de " + player1Name);
        player2Tabuleiro = new TabuleiroPanel(player2Shots, "Tabuleiro de " + player2Name);

        // Exemplo de embarcações do jogador 2
        player2Embarcacoes[2][3] = true;
        player2Embarcacoes[2][4] = true;
        player2Embarcacoes[2][5] = true;

        player2Embarcacoes[7][8] = true;
        player2Embarcacoes[8][8] = true;
        player2Embarcacoes[9][8] = true;

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
                        if (player2Embarcacoes[i][j]) {
                            g2d.setColor(Color.RED); // Tiro que acertou uma embarcação
                        } else {
                            g2d.setColor(Color.BLUE); // Tiro na água
                        }
                        g2d.fill(cell);
                    } else if (selectedRow == i && selectedCol == j && this == player2Tabuleiro) {
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
        if (tabuleiroPanel == player2Tabuleiro) { // Permite clique apenas no tabuleiro do jogador 2
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
                selectedRow = row;
                selectedCol = col;
                tabuleiroPanel.repaint();  // Repinta o painel para mostrar a seleção
                System.out.println("Selected cell: (" + row + ", " + col + ")");
            }
        }
    }

    private void handleShootButton() {
        // Lógica para a ação do botão "Atirar"
        if (selectedRow != -1 && selectedCol != -1) {
            if (!player2Shots[selectedRow][selectedCol]) { // Verifica se o tiro já foi feito
                player2Shots[selectedRow][selectedCol] = true;
                if (player2Embarcacoes[selectedRow][selectedCol]) {
                    Set<Point> embarcacao = verificarAfundamento(selectedRow, selectedCol);
                    if (embarcacao != null) {
                        for (Point p : embarcacao) {
                            player2Shots[p.x][p.y] = true;
                        }
                    }
                }
                repaint();
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

    private Set<Point> verificarAfundamento(int row, int col) {
        // Verifica se uma embarcação foi afundada
        Set<Point> embarcacao = new HashSet<>();
        if (player2Embarcacoes[row][col]) {
            // Adiciona a posição inicial
            embarcacao.add(new Point(row, col));
            // Verifica as posições ao redor
            verificarPosicao(row - 1, col, embarcacao);
            verificarPosicao(row + 1, col, embarcacao);
            verificarPosicao(row, col - 1, embarcacao);
            verificarPosicao(row, col + 1, embarcacao);
        }
        // Verifica se todos os pontos da embarcação foram atingidos
        for (Point p : embarcacao) {
            if (!player2Shots[p.x][p.y]) {
                return null; // Não foi afundada
            }
        }
        return embarcacao; // Foi afundada
    }

    private void verificarPosicao(int row, int col, Set<Point> embarcacao) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && player2Embarcacoes[row][col]) {
            embarcacao.add(new Point(row, col));
            // Verifica recursivamente as posições ao redor
            verificarPosicao(row - 1, col, embarcacao);
            verificarPosicao(row + 1, col, embarcacao);
            verificarPosicao(row, col - 1, embarcacao);
            verificarPosicao(row, col + 1, embarcacao);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TabuleiroTiro frame = new TabuleiroTiro("Jogador 1", "Jogador 2");
            frame.setVisible(true);
        });
    }
}
