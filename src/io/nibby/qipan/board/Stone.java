package io.nibby.qipan.board;

import javafx.scene.canvas.GraphicsContext;

/*
    This is the board instance of a stone.
    Much of the code here concerns itself with its visual appearance on screen.
 */
public class Stone {

    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int x, y;
    private int color;

    // Drawing properties
    // These are calculated upon being added to the board.
    private double drawX, drawY;
    private double wobble;

    public Stone(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void onPlace(BoardMetrics metrics) {
        // TODO account for fuzzy placement & wobble
        drawX = metrics.getGridX(getX());
        drawY = metrics.getGridY(getY());
    }

    public void wobble(double wobble) {
        this.wobble = wobble;
    }

    public int getColor() {
        return color;
    }

    public void render(GraphicsContext g, BoardMetrics metrics) {
        // TODO temporary style, change later
        StoneStyle style = StoneStyle.PLAIN;
        style.render(g, this, metrics);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDrawX() {
        return drawX;
    }

    public double getDrawY() {
        return drawY;
    }

    public void setDrawX(double drawX) {
        this.drawX = drawX;
    }

    public void setDrawY(double drawY) {
        this.drawY = drawY;
    }
}
