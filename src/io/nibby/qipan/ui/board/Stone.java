package io.nibby.qipan.ui.board;

import javafx.scene.canvas.GraphicsContext;

import java.util.Random;

/*
    This is the board instance of a stone.
    Much of the code here concerns itself with its visual appearance on screen.
 */
public class Stone {

    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int x, y;
    private int color;

    // Radial gradient drawing properties (for textured stones)
    private double wobble, wobbleMax;
    protected double rgFocusAngle = 250d;
    protected double rgFocusDistance = 0.1d;
    protected double rgCenterX = 0.35d;
    protected double rgCenterY = 0.35d;
    protected double rgRadius = 0.45d;
    protected double wobbleX = 0, wobbleY = 0;
    protected double fuzzyX = 0, fuzzyY = 0;

    public Stone(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void onPlace(BoardMetrics metrics) {
        // TODO account for fuzzy placement & wobble
        nudge((int) (Math.random() * 2) - 2, (int) (Math.random() * 2) - 2, metrics);
    }

    public void setWobble(double wobble) {
        this.wobble = wobble;
        this.wobbleMax = wobble;
        // TODO temp slide
    }

    public void wobble() {
        if (wobble <= 0) {
            return;
        }

        Random r = new Random();
        double w = wobble / wobbleMax;
        wobbleX = r.nextDouble() * wobble;
        wobbleY = r.nextDouble() * wobble;
        rgFocusAngle += r.nextDouble() * (w * 1d) / 2;
        rgCenterX += r.nextDouble() * (w * 0.05d) - (w * 0.05d) / 2;
        rgCenterY += r.nextDouble() * (w * 0.05d) - (w * 0.05d) / 2;
        wobble -= r.nextDouble() * 0.01d + 0.05d;

        if (wobble <= 0) {
            rgCenterX = 0.35d + r.nextDouble() * 0.05d;
            rgCenterY = 0.35d + r.nextDouble() * 0.05d;
            rgRadius = 0.45d;
            rgFocusDistance = 0.1d;
            rgFocusAngle = 250d;
        }
    }

    public void nudge(int xDiff, int yDiff, BoardMetrics metrics) {
        double margin = metrics.stoneSize / 20;
        fuzzyX = Math.random() * margin * xDiff;
        fuzzyY = Math.random() * margin * yDiff;
    }

    public int getColor() {
        return color;
    }

    public void render(GraphicsContext g, BoardMetrics metrics, StoneStyle stoneStyle) {
        stoneStyle.render(g, this, metrics);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean shouldWobble() {
        return wobble > 0d;
    }

}
