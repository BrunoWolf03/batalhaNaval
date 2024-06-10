package View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

public class Embarcacao {
    private List<Rectangle2D.Double> cells;
    private Color color;
    private int tipoNavio;
    private int orientacao; // 0 para leste, 1 para sul, 2 para oeste, 3 para norte
    private boolean isErrored; // Flag para marcar erro

    public Embarcacao(List<Rectangle2D.Double> cells, Color color, int tipoNavio) {
        this.cells = cells;
        this.color = color;
        this.tipoNavio = tipoNavio;
        this.orientacao = 0; // Inicia com leste
        this.isErrored = false; // Inicialmente não está com erro
    }

    public List<Rectangle2D.Double> getCells() {
        return cells;
    }

    public Color getColor() {
        return isErrored ? Color.RED : color; // Retorna vermelho se houver erro
    }

    public int getTipoNavio() {
        return tipoNavio;
    }

    public int getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(int orientacao) {
        this.orientacao = orientacao;
    }

    public String getCoordenadaInicial(int startX, int startY) {
        Rectangle2D.Double firstCell = cells.get(0);
        int col = (int) ((firstCell.getX() - startX) / Tabuleiro.CELL_SIZE);
        int row = (int) ((firstCell.getY() - startY) / Tabuleiro.CELL_SIZE);
        char letter = (char) ('A' + row);
        int number = col + 1;
        return "" + letter + number;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(getColor());
        for (Rectangle2D.Double cell : cells) {
            g2d.fill(cell);
            g2d.setColor(getColor());
            g2d.draw(cell);
        }
    }

    public boolean contains(double x, double y) {
        for (Rectangle2D.Double cell : cells) {
            if (cell.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public void move(double dx, double dy) {
        for (Rectangle2D.Double cell : cells) {
            cell.setRect(cell.getX() + dx, cell.getY() + dy, cell.getWidth(), cell.getHeight());
        }
    }

    public void rotate(double anchorX, double anchorY, double cellSize) {
        for (Rectangle2D.Double cell : cells) {
            double relativeX = cell.getX() - anchorX;
            double relativeY = cell.getY() - anchorY;
            double newRelativeX = -relativeY;
            double newRelativeY = relativeX;
            cell.setRect(anchorX + newRelativeX, anchorY + newRelativeY, cellSize, cellSize);
        }
        // Alterna a orientação
        orientacao = (orientacao + 1) % 4;
    }


    public void setErrored(boolean errored) {
        isErrored = errored;
    }

    public boolean isErrored() {
        return isErrored;
    }
}
