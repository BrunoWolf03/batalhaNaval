package View;

import Controller.Controller;
import Model.Jogador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.WHITE;

public class Tabuleiro extends JFrame {
    public static final int SIZE = 15;
    public static final int CELL_SIZE = 30;

    private String player1Name;
    private String player2Name;

    private List<Embarcacao> ships;
    private Embarcacao selectedShip;
    private boolean isShipSelected;
    private int currentPlayer;
    private boolean isConfirming;

    private Controller controller;

    public Tabuleiro(String player1Name, String player2Name, Controller controller) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.controller = controller;
        controller.setTabuleiro(this);

        ships = new ArrayList<>();
        selectedShip = null;
        isShipSelected = false;
        currentPlayer = 1; // Começa com o jogador 1
        isConfirming = false;

        setTitle("Batalha Naval");
        setSize(SIZE * CELL_SIZE + 300, SIZE * CELL_SIZE + 70);
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
        tabuleiroPanel.setFocusable(true);
        add(tabuleiroPanel);
        tabuleiroPanel.requestFocusInWindow();

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

            int startX = getBoardStartX();
            int startY = getBoardStartY();
            for (int j = 0; j < SIZE; j++) {
                String num = Integer.toString(j + 1);
                int numWidth = g2d.getFontMetrics().stringWidth(num);
                g2d.drawString(num, startX + j * CELL_SIZE + (CELL_SIZE - numWidth) / 2, startY - 5);
            }

            g2d.drawString("Jogador 1: " + player1Name, 10, 20);
            g2d.drawString("Jogador 2: " + player2Name, 10, 40);
            g2d.drawString("Jogador atual: " + (currentPlayer == 1 ? player1Name : player2Name), 10, 60);

            for (int i = 0; i < SIZE; i++) {
                char letter = (char) ('A' + i);
                int letterHeight = g2d.getFontMetrics().getAscent();
                g2d.drawString(Character.toString(letter), startX - 20, startY + i * CELL_SIZE + (CELL_SIZE + letterHeight) / 2);
            }

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Rectangle2D.Double cell = new Rectangle2D.Double(startX + j * CELL_SIZE, startY + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2d.setColor(WHITE);
                    g2d.fill(cell);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(cell);
                }
            }

            for (Embarcacao ship : ships) {
                ship.draw(g2d);
            }

            if (selectedShip != null) {
                g2d.setColor(selectedShip.getColor());
                for (Rectangle2D.Double cell : selectedShip.getCells()) {
                    g2d.fill(cell);
                    g2d.setColor(selectedShip.getColor());
                    g2d.draw(cell);
                }
            }
        }
    }

    public int getBoardStartX() {
        int panelWidth = getWidth();
        int tabuleiroWidth = SIZE * CELL_SIZE;
        return panelWidth - tabuleiroWidth - 20;
    }

    public int getBoardStartY() {
        int panelHeight = getHeight();
        int tabuleiroHeight = SIZE * CELL_SIZE;
        return (panelHeight - tabuleiroHeight) / 2;
    }

    private void handleMouseClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!isShipSelected) {
                for (Embarcacao ship : ships) {
                    if (ship.contains(x, y)) {
                        selectedShip = ship;
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
                int startX = panelWidth - tabuleiroWidth +10 ;
                int startY = ((panelHeight - tabuleiroHeight) / 2) ;

                int col = (x - startX) / CELL_SIZE;
                int row = (y - startY) / CELL_SIZE;

                if (col >= 0 && col < SIZE && row >= 0 && row < SIZE) {
                    double dx = startX + col * CELL_SIZE - selectedShip.getCells().get(0).getX();
                    double dy = startY + row * CELL_SIZE - selectedShip.getCells().get(0).getY();
                    selectedShip.move(dx, dy);
                    isShipSelected = false;
                    selectedShip = null;
                    repaint();
                }
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (!isShipSelected) {
                for (Embarcacao ship : ships) {
                    if (ship.contains(x, y)) {
                        selectedShip = ship;
                        break;
                    }
                }
            }

            if (selectedShip != null) {
                // Ponto de ancoragem: extremidade esquerda da embarcação (primeira célula)
                Rectangle2D.Double anchorCell = selectedShip.getCells().get(0);
                double anchorX = anchorCell.getX();
                double anchorY = anchorCell.getY();

                selectedShip.rotate(anchorX, anchorY, CELL_SIZE);
                repaint();
            }
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && selectedShip != null) {
            isShipSelected = false;
            selectedShip = null;
            repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!isConfirming) {
                int option = JOptionPane.showConfirmDialog(this, "Confirma o posicionamento das suas embarcações?", "Confirmação", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    isConfirming = true;
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
    }

    public boolean isShipSelected() {
        return isShipSelected;
    }

    public void deselectShip() {
        isShipSelected = false;
        selectedShip = null;
        repaint();
    }

    public void initializeShips() {
        ships.clear();

        int shipStartX = 50;
        int shipStartY = 100;
        for (int i = 0; i < 4; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            int x = shipStartX + i * 2 * CELL_SIZE;
            int y = shipStartY;
            cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            ships.add(new Embarcacao(cells, new Color(144, 238, 144)));
        }

        for (int i = 0; i < 3; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                int x = shipStartX + i * 3 * CELL_SIZE + j * CELL_SIZE;
                int y = shipStartY + CELL_SIZE * 2;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.YELLOW));
        }

        for (int i = 0; i < 2; i++) {
            List<Rectangle2D.Double> cells = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                int x = shipStartX + i * 5 * CELL_SIZE + j * CELL_SIZE;
                int y = shipStartY + CELL_SIZE * 4;
                cells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
            }
            ships.add(new Embarcacao(cells, Color.ORANGE));
        }

        List<Rectangle2D.Double> redShipCells = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            int x = shipStartX + j * CELL_SIZE;
            int y = shipStartY + CELL_SIZE * 6;
            redShipCells.add(new Rectangle2D.Double(x, y, CELL_SIZE, CELL_SIZE));
        }
        ships.add(new Embarcacao(redShipCells, Color.PINK));

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


}
