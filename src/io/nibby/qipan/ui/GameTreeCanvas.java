package io.nibby.qipan.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * A UI component that displays the in-game move tree graphically.
 */
public class GameTreeCanvas extends Canvas implements Renderable {

    private GameTreeUI parent;
    private GraphicsContext g;

    public GameTreeCanvas(GameTreeUI parent) {
        this.g = getGraphicsContext2D();
        this.parent = parent;
    }

    public void render() {
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        //TODO temporary
        // TODO Render only those in visible regions
        for (int moveNum : parent.getNodeData().keySet()) {
            List<GameTreeNode> moveItems = parent.getNodeData().get(moveNum);
            for (GameTreeNode item : moveItems) {
                item.renderLines(g);
            }
        }

        for (int moveNum : parent.getNodeData().keySet()) {
            List<GameTreeNode> moveItems = parent.getNodeData().get(moveNum);
            for (GameTreeNode item : moveItems) {
                item.render(g);
            }
        }
    }


}
