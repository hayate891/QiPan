package io.nibby.qipan.ui;

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
public class GameTreeNode {

    // Size constants (pixels)N
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
    private GameTreeNode parent;

    public GameTreeNode(GameTreeUI treeUi, GameTreeNode parent, double x, double y, MoveNode node) {
        this.parent = parent;
        setTreeUI(treeUi);
        setX(x);
        setY(y);
        setNode(node);
    }

    public void renderLines(GraphicsContext g) {
        double iconSize = getWidth() / 2 < getHeight() / 2 ? getWidth() / 2 : getHeight() / 2;
        double ox = treeUI.getXOffset();
        double oy = treeUI.getYOffset();

        // TODO temporary
        if (node.equals(treeUI.getCurrentMove())) {
            g.setFill(Color.LIGHTBLUE);
            g.fillRect(getX() + ox, getY() + oy, getWidth(), getHeight());
        }
        if (parent != null) {
            g.setStroke(Color.LIGHTGRAY);
            g.setLineWidth(2d);
            /*
                When drawing connection lines, it's important to keep the lines tidy if one parent node
                has multiple children. A straight line is only drawn for the first two parent-child connections.
                The rest of the lines will be chained through the 2nd child to ensure cleaner presentation.
             */
            if (getDisplayColumn() - parent.getDisplayColumn() > 1) {
                double x1 = ox + parent.getX() + getWidth() / 2;
                double y1 = oy + parent.getY() + getHeight() / 2;
                g.strokeLine(x1, y1, getX() + getWidth() / 2 + ox, y1);
                g.strokeLine(getX() + getWidth() / 2 + ox, y1, getX() + getWidth() / 2 + ox, getY() + getHeight() / 2 + oy);
            } else {
                double x1 = ox + parent.getX() + getWidth() / 2;
                double y1 = oy + parent.getY() + getHeight() / 2;
                double x2 = ox + getX() + getWidth() / 2;
                double y2 = oy + getY() + getHeight() / 2;
                g.strokeLine(x1, y1, x2, y2);
            }
            g.setLineWidth(1d);
        }
    }

    public void render(GraphicsContext g) {
        double iconSize = getWidth() / 2 < getHeight() / 2 ? getWidth() / 2 : getHeight() / 2;
        // Stone icon
        // TODO Make this more sexy later
        double ox = treeUI.getXOffset();
        double oy = treeUI.getYOffset();
        double x = ox + getX() + getWidth() / 2 - iconSize / 2;
        double y = oy + getY() + getHeight() / 2 - iconSize / 2;
        g.setFill(STONE_COLORS[node.getMoveNumber() % 2]);
        g.fillOval(x, y, iconSize, iconSize);
        g.setStroke(STONE_COLORS[(node.getMoveNumber() + 1) % 2]);
        g.strokeOval(x, y, iconSize, iconSize);
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

    public int getDisplayColumn() {
        return displayRow;
    }

    public void setDisplayColumn(int column) {
        this.displayRow = column;
        x = GameTreeUI.DRAW_X_MARGIN + getDisplayColumn() * DISPLAY_HEIGHT;
    }

    public GameTreeNode getParent() {
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
