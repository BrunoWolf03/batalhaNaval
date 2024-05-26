package View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

class Embarcacao {
    private List<Rectangle2D.Double> cells;
    private Color color;

    public Embarcacao(List<Rectangle2D.Double> cells, Color color) {
        this.cells = cells;
        this.color = color;
    }

    public List<Rectangle2D.Double> getCells() {
        return cells;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
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

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        for (Rectangle2D.Double cell : cells) {
            g2d.fill(cell);
            g2d.setColor(color);
            g2d.draw(cell);
        }
    }

    public void rotate(int cellSize) {
        double centerX = 0;
        double centerY = 0;

        // Calculate the center of the ship
        for (Rectangle2D.Double cell : cells) {
            centerX += cell.getX() + cellSize / 2;
            centerY += cell.getY() + cellSize / 2;
        }
        centerX /= cells.size();
        centerY /= cells.size();

        // Rotate each cell around the center
        for (Rectangle2D.Double cell : cells) {
            double relativeX = cell.getX() - centerX + cellSize / 2;
            double relativeY = cell.getY() - centerY + cellSize / 2;
            double rotatedX = -relativeY;
            double rotatedY = relativeX;
            cell.setRect(centerX + rotatedX - cellSize / 2 , centerY + rotatedY - cellSize / 2, cellSize, cellSize);
        }
    }
}