package io.nibby.qipan.board;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static io.nibby.qipan.board.Stone.BLACK;
import static io.nibby.qipan.board.Stone.WHITE;

public enum StoneStyle {

    PLAIN("Plain") {
        @Override
        public void render(GraphicsContext g, Stone stone, BoardMetrics metrics) {
            // TODO make this customizable
            double x = stone.getDrawX();
            double y = stone.getDrawY();
            switch(stone.getColor()) {
                case BLACK:
                    g.setFill(Color.BLACK);
                    g.fillOval(x, y, metrics.stoneSize, metrics.stoneSize);
                    break;
                case WHITE:
                    g.setFill(Color.WHITE);
                    g.fillOval(x, y, metrics.stoneSize, metrics.stoneSize);
                    g.setStroke(Color.BLACK);
                    g.strokeOval(x, y, metrics.stoneSize, metrics.stoneSize);
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
    },

    // CERAMIC bi-convex 35mm
    CERAMIC("Ceramic") {
        @Override
        public void render(GraphicsContext g, Stone stone, BoardMetrics metrics) {
            switch(stone.getColor()) {
                case BLACK:

                    break;
                case WHITE:

                    break;
            }
        }

        @Override
        public double wobbleMargin() {
            return 3.5d;
        }

        @Override
        public boolean fuzzyPlacement() {
            return true;
        }
    },

    yunzi("Yun Zi") {
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
    };

    private String name;

    StoneStyle(String name) {
        this.name = name;
    }

    public abstract void render(GraphicsContext g, Stone stone, BoardMetrics metrics);
    public abstract double wobbleMargin();
    public abstract boolean fuzzyPlacement();
}
