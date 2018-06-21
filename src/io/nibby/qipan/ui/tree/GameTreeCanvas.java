package io.nibby.qipan.ui.tree;

import io.nibby.qipan.ui.Renderable;
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
        for (int moveNum : parent.getItemData().keySet()) {
            List<MoveNodeItem> moveItems = parent.getItemData().get(moveNum);
            for (MoveNodeItem item : moveItems) {
                item.renderLines(g);
            }
        }

        for (int moveNum : parent.getItemData().keySet()) {
            List<MoveNodeItem> moveItems = parent.getItemData().get(moveNum);
            for (MoveNodeItem item : moveItems) {
                item.render(g);
            }
        }
    }


}
