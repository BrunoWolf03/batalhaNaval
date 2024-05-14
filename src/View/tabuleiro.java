package View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class tabuleiro extends JFrame {
    private static final int SIZE = 15;  // Tamanho do tabuleiro
    private static final int CELL_SIZE = 30;  // Tamanho de cada célula

    public tabuleiro() {
        setTitle("Batalha Naval");
        setSize(SIZE * CELL_SIZE + 50, SIZE * CELL_SIZE + 70);  // Dimensões da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TabuleiroPanel tabuleiroPanel = new TabuleiroPanel();
        add(tabuleiroPanel);
    }

    private class TabuleiroPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Desenhar células do tabuleiro
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    Rectangle2D.Double cell = new Rectangle2D.Double(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g2d.setColor(Color.BLUE);  // Cor do contorno da célula
                    g2d.draw(cell);  // Desenhar o contorno da célula
                    g2d.setColor(Color.CYAN);  // Cor do preenchimento da célula
                    g2d.fill(cell);  // Preencher a célula
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            tabuleiro gui = new tabuleiro();
            gui.setVisible(true);
        });
    }
}
