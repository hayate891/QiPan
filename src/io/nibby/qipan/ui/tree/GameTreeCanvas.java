package io.nibby.qipan.ui.tree;

import io.nibby.qipan.game.Game;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * A UI component that displays the in-game move tree graphically.
 */
public class GameTreeCanvas extends Canvas {

    private Game game;
    private GraphicsContext g;

    public GameTreeCanvas(GameTreeUI parent) {
        this.g = getGraphicsContext2D();
    }

    public void render() {
        g.clearRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        render();
    }

}
