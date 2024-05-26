package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.WHITE;

public class Tabuleiro extends JFrame {
    private static final int SIZE = 15;  // Tamanho do tabuleiro
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula

    private String player1Name;
    private String player2Name;

    private List<Embarcacao> ships;
    private Embarcacao selectedShip;
    private Color originalColor;
    private boolean isShipSelected;
    private int currentPlayer;
    private boolean isConfirming;

    public Tabuleiro(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;

        ships = new ArrayList<>();
        selectedShip = null;
        isShipSelected = false;
        currentPlayer = 1; // Começa com o jogador 1
        isConfirming = false;

        setTitle("Batalha Naval");
        setSize(SIZE * CELL_SIZE + 300, SIZE * CELL_SIZE + 70);  // Dimensões da janela (aumentada a largura)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TabuleiroPanel tabuleiroPanel = new TabuleiroPanel();
        tabuleiroPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
        tabuleiroPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
        tabuleiroPanel.setFocusable(true); // Para garantir que o painel possa receber o foco do teclado
        add(tabuleiroPanel);

        // Solicite o foco do teclado para o painel
        tabuleiroPanel.requestFocusInWindow();

        // Inicializar embarcações
        initializeShips();
    }

    private class TabuleiroPanel extends JPanel {

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int tabuleiroWidth = SIZE * CELL_SIZE;
            int tabuleiroHeight = SIZE * CELL_SIZE;

            // Calcula as coordenadas para centralizar o tabuleiro no lado direito
            int startX = panelWidth - tabuleiroWidth - 20;  // 20 pixels de margem da borda direita
            int startY = (panelHeight - tabuleiroHeight) / 2;

            // Desenhar números na parte superior
            for (int j = 0; j < SIZE; j++) {
                String num = Integer.toString(j + 1);
                int numWidth = g2d.getFontMetrics().stringWidth(num);
                g2d.drawString(num, startX + j * CELL_SIZE + (CELL_SIZE - numWidth) / 2, startY - 5);
            }

            g2d.drawString("Jogador 1: " + player1Name, 10, 20);
            g2d.drawString("Jogador 2: " + player2Name, 10, 40);
            g2d.drawString("Jogador atual: " + (currentPlayer == 1 ? player1Name : player2Name), 10, 60);

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
                }
            }

            // Desenhar as embarcações
            for (Embarcacao ship : ships) {
                ship.draw(g2d);
            }

            if (selectedShip != null) {
                g2d.setColor(Color.GRAY);
                for (Rectangle2D.Double cell : selectedShip.getCells()) {
                    g2d.fill(cell);
                    g2d.setColor(Color.GRAY);
                    g2d.draw(cell);
                }
            }
        }
    }

    private void handleMouseClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!isShipSelected) {
                for (Embarcacao ship : ships) {
                    if (ship.contains(x, y)) {
                        selectedShip = ship;
                        originalColor = ship.getColor();
                        isShipSelected = true;
                        repaint();
                        break;
                    }
                }
            } else {
                int panelWidth = getWidth();
                int panelHeight = getHeight();
                int tabuleiroWidth = SIZE * CELL_SIZE;
                int tabuleiroHeight = SIZE * CELL_SIZE;
                int startX = panelWidth - tabuleiroWidth - 5;
                int startY = ((panelHeight - tabuleiroHeight) / 2) - 20;

                int col = (x - startX) / CELL_SIZE;
                int row = (y - startY) / CELL_SIZE;

                if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
                    double dx = startX + col * CELL_SIZE - selectedShip.getCells().get(0).getX();
                    double dy = startY + row * CELL_SIZE - selectedShip.getCells().get(0).getY();
                    selectedShip.move(dx, dy);
                    isShipSelected = false;
                    selectedShip.setColor(originalColor);
                    selectedShip = null;
                    repaint();
                }
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (selectedShip != null && selectedShip.contains(x, y)) {
                selectedShip.rotate(CELL_SIZE);
                repaint();
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && selectedShip != null) {
            isShipSelected = false;
            selectedShip.setColor(originalColor);
            selectedShip = null;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!isConfirming) {
                int option = JOptionPane.showConfirmDialog(this, "Confirma o posicionamento das suas embarcações?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    isConfirming = true;
                }
            } else {
                if (currentPlayer == 1) {
                    currentPlayer = 2;
                    initializeShips();  // Limpa e inicializa as embarcações para o jogador 2
                    isConfirming = false;
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Ambos os jogadores posicionaram suas embarcações. O jogo pode começar!");
                    dispose();
                }
            }
        }
    }

    private void initializeShips() {
        ships.clear();  // Limpar a lista de embarcações

        // Adicionar 4 embarcações de 1 quadrado cada de cor verde clara na lista
        int shipStartX = 50;
        int shipStartY = 50 + 4 * CELL_SIZE;
        for (int i = 0; i < 4; i++) {
            int x = shipStartX + i * 2 * CELL_SIZE;
            int y = shipStartY;
            List<Rectangle2D.Double> cells = new ArrayList<>();
            cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            ships.add(new Embarcacao(cells, new Color(144, 238, 144)));
        }

        // Adicionar 3 embarcações de 2 quadrados cada de cor amarela na lista
        for (int i = 0; i < 3; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                int x = shipStartX + i * 3 * CELL_SIZE + j * CELL_SIZE;
                int y = shipStartY + CELL_SIZE * 2;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.YELLOW));
        }

        // Adicionar 2 embarcações de 4 quadrados cada de cor laranja na lista
        for (int i = 0; i < 2; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                int x = shipStartX + i * 5 * CELL_SIZE + j * CELL_SIZE;
                int y = shipStartY + CELL_SIZE * 4;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.ORANGE));
        }

        // Adicionar 1 embarcação de 5 quadrados de cor vermelha na lista
        List<Rectangle2D.Double> redShipCells = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            int x = shipStartX + j * CELL_SIZE;
            int y = shipStartY + CELL_SIZE * 6;
            redShipCells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
        }
        ships.add(new Embarcacao(redShipCells, Color.PINK));

        // Adicionar 5 embarcações de hidroaviões (estrutura em T) na lista
        for (int i = 0; i < 5; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            int baseX = shipStartX + i * (3 * CELL_SIZE + CELL_SIZE);
            int baseY = shipStartY + CELL_SIZE * 8;
            int[][] coords = {{0, 1}, {1, 0}, {2, 1}};
            for (int[] coord : coords) {
                int x = baseX + coord[0] * CELL_SIZE;
                int y = baseY + coord[1] * CELL_SIZE;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.BLUE));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame panel = null;
            InserirNome gui = new InserirNome(panel);
            gui.setVisible(true);
            Tabuleiro gui2 = new Tabuleiro(gui.nome1, gui.nome2);
            gui2.setVisible(true);
        });
    }
}
