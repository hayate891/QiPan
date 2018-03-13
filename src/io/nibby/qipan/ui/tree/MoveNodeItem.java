package io.nibby.qipan.ui.tree;

import io.nibby.qipan.game.MoveNode;
import io.nibby.qipan.ui.board.Stone;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * A small canvas representation of the move node in the game tree.
 * Most of the data stored here is related to its rendering.
 *
 * The widget has two main draw modes: DISPLAY_FULL and DISPLAY_SMALL.
 * DISPLAY_FULL draws the move node with as much info as possible.
 * DISPLAY_SMALL draws the move node with an icon and move co-ordinate only.
 */
public class MoveNodeItem {

    // Size constants (pixels)
    public static final int DISPLAY_WIDTH = 24;
    public static final int DISPLAY_HEIGHT = 24;

    private static final Color[] STONE_COLORS = { Color.BLACK, Color.WHITE };

    /*
        Display co-ordinates on canvas.
     */
    private double x;
    private double y;
    private int displayRow;
    private MoveNode node;
    private GameTreeUI treeUI;
    private MoveNodeItem parent;

    public MoveNodeItem(GameTreeUI treeUi, MoveNodeItem parent, double x, double y, MoveNode node) {
        this.parent = parent;
        setTreeUI(treeUi);
        setX(x);
        setY(y);
        setNode(node);
    }

    public void renderLines(GraphicsContext g) {
        double iconSize = getWidth() / 2 < getHeight() / 2 ? getWidth() / 2 : getHeight() / 2;

        // TODO temporary
        if (node.equals(treeUI.getCurrentMove())) {
            g.setFill(Color.LIGHTBLUE);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
        if (parent != null) {
            g.setStroke(Color.LIGHTGRAY);
            g.setLineWidth(2d);
            /*
                When drawing connection lines, it's important to keep the lines tidy if one parent node
                has multiple children. A straight line is only drawn for the first two parent-child connections.
                The rest of the lines will be chained through the 2nd child to ensure cleaner presentation.
             */
            if (parent != null && parent.getNode().getChildren().indexOf(node) < 2) {
                g.strokeLine(parent.getX() + getWidth() / 2, parent.getY() + getHeight() / 2,
                        getX() + getWidth() / 2, getY() + getHeight() / 2);
            } else {
                // This is the 2nd+ child that is spanning multiple rows from its parent
                // its connection will be facilitated by a direct connection to the position of the child above.
                double lastChildX = GameTreeUI.DRAW_X_MARGIN + node.getMoveNumber() * DISPLAY_WIDTH;
                double lastChildY = GameTreeUI.DRAW_Y_MARGIN + (getDisplayRow() - 1) * DISPLAY_HEIGHT;

                g.strokeLine(lastChildX + getWidth() / 2, lastChildY + getHeight() / 2,
                        getX() + getWidth() / 2, getY() + getHeight() / 2);
            }
            g.setLineWidth(1d);
        }
    }

    public void render(GraphicsContext g) {
        double iconSize = getWidth() / 2 < getHeight() / 2 ? getWidth() / 2 : getHeight() / 2;
        // Stone icon
        // TODO Make this more sexy later
        g.setFill(STONE_COLORS[node.getNextColor() == Stone.BLACK ? 1 : 0]);
        g.fillOval(getX() + getWidth() / 2 - iconSize / 2, getY() + getHeight() / 2 - iconSize / 2, iconSize, iconSize);
        g.setStroke(STONE_COLORS[(node.getNextColor() + 1) % 2]);
        g.strokeOval(getX() + getWidth() / 2 - iconSize / 2, getY() + getHeight() / 2 - iconSize / 2, iconSize, iconSize);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return DISPLAY_WIDTH;
    }

    public double getHeight() {
        return DISPLAY_HEIGHT;
    }

    public int getDisplayRow() {
        return displayRow;
    }

    public void setDisplayRow(int displayRow) {
        this.displayRow = displayRow;
    }

    public MoveNodeItem getParent() {
        return parent;
    }

    public MoveNode getNode() {
        return node;
    }

    public void setNode(MoveNode node) {
        this.node = node;
    }

    public GameTreeUI getTreeUI() {
        return treeUI;
    }

    public void setTreeUI(GameTreeUI treeUI) {
        this.treeUI = treeUI;
    }
}
