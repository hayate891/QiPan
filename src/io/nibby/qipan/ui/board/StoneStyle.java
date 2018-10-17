package io.nibby.qipan.ui.board;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

import static io.nibby.qipan.ui.board.Stone.BLACK;
import static io.nibby.qipan.ui.board.Stone.WHITE;

public enum StoneStyle {

    PLAIN("Plain") {
        @Override
        public void render(GraphicsContext g, Stone stone, BoardMetrics metrics) {
            // TODO make this customizable
            double x = metrics.getBoardStoneX(stone.getX());
            double y = metrics.getBoardStoneY(stone.getY());
            switch(stone.getColor()) {
                case BLACK:
                    g.setFill(Color.BLACK);
                    g.fillOval(x, y, metrics.stoneSize, metrics.stoneSize);
                    break;
                case WHITE:
                    g.setFill(Color.WHITE);
                    g.fillOval(x, y, metrics.stoneSize / 20 * 19, metrics.stoneSize / 20 * 19);
                    g.setStroke(Color.BLACK);
                    g.strokeOval(x, y, metrics.stoneSize / 20 * 19, metrics.stoneSize / 20 * 19);
                    break;
            }
        }

        @Override
        public double wobbleMargin() {
            return 0;
        }

        @Override
        public boolean fuzzyPlacement() {
            return false;
        }

        @Override
        public Color annotationColor(Stone stone) {
            return stone.getColor() == Stone.BLACK ? Color.WHITE : Color.BLACK;
        }
    },

    // CERAMIC bi-convex
    CERAMIC("Ceramic") {

        @Override
        public void render(GraphicsContext g, Stone stone, BoardMetrics metrics) {
            double o = metrics.stoneSize / 16;
            double x = metrics.getBoardStoneX(stone.getX()) + stone.wobbleX + stone.fuzzyX + o;
            double y = metrics.getBoardStoneY(stone.getY()) + stone.wobbleY + stone.fuzzyY + o;
            RadialGradient gradient;
            DropShadow shadow = new DropShadow();
            shadow.setRadius(metrics.stoneSize / 8);
            shadow.setOffsetX(metrics.stoneSize / 12);
            shadow.setOffsetY(metrics.stoneSize / 12);
            shadow.setBlurType(BlurType.GAUSSIAN);
            shadow.setColor(Color.color(0.15f, 0.15f, 0.15f, 0.5f));
            g.setEffect(shadow);

            switch(stone.getColor()) {
                case BLACK:
                    gradient = new RadialGradient(stone.rgFocusAngle, stone.rgFocusDistance, stone.rgCenterX, stone.rgCenterY,
                            stone.rgRadius, true, CycleMethod.NO_CYCLE,
                            new Stop(0d, Color.color(0.45d, 0.45d, 0.45d, 1d)),
                            new Stop(0.99d, Color.color(0.1d, 0.1d, 0.0d, 1d)));
                    g.setFill(gradient);

                    g.setEffect(shadow);
                    g.fillOval(x, y, metrics.stoneSize, metrics.stoneSize);
                    break;
                case WHITE:
                    gradient = new RadialGradient(stone.rgFocusAngle, stone.rgFocusDistance, stone.rgCenterX, stone.rgCenterY,
                            stone.rgRadius, true, CycleMethod.NO_CYCLE,
                            new Stop(0d, Color.color(0.99d, 0.99d, 0.99d, 1d)),
                            new Stop(0.99d, Color.color(0.90d, 0.90d, 0.90d, 1d)));
                    g.setFill(gradient);

                    g.setEffect(shadow);
                    g.fillOval(x, y, metrics.stoneSize / 20 * 19, metrics.stoneSize / 20 * 19);
                    g.setEffect(null);
                    break;
            }
            g.setEffect(null);
        }

        @Override
        public double wobbleMargin() {
            return 2d;
        }

        @Override
        public boolean fuzzyPlacement() {
            return true;
        }

        @Override
        public Color annotationColor(Stone stone) {
            return stone.getColor() == Stone.BLACK ? Color.WHITE : Color.BLACK;
        }
    },

    YUNZI("Yun Zi") {
        @Override
        public void render(GraphicsContext g, Stone stone, BoardMetrics metrics) {

        }

        @Override
        public double wobbleMargin() {
            return 0d;
        }

        @Override
        public boolean fuzzyPlacement() {
            return true;
        }

        @Override
        public Color annotationColor(Stone stone) {
            return null;
        }
    };

    private String name;

    StoneStyle(String name) {
        this.name = name;
    }

    public abstract void render(GraphicsContext g, Stone stone, BoardMetrics metrics);
    public abstract double wobbleMargin();
    public abstract boolean fuzzyPlacement();
    public abstract Color annotationColor(Stone stone);

    public static StoneStyle getDefault() {
        return CERAMIC;
    }
}
