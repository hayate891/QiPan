package io.nibby.qipan.ui.tree;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameListener;
import io.nibby.qipan.game.MoveNode;
import io.nibby.qipan.ui.CanvasContainer;
import io.nibby.qipan.ui.board.Stone;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.util.*;

/**
 * The super-level parent of the game tree component.
 * It consists of a tree canvas and two scroll-bars.
 */
public class GameTreeUI extends BorderPane implements GameListener {

    private Game game;
    private CanvasContainer container;
    private GameTreeCanvas canvas;
    private ScrollBar hScroll; // Horizontal
    private ScrollBar vScroll; // Vertical
    private FlowPane corner;
    private Map<Integer, List<MoveNodeItem>> itemData = new HashMap<>();
    private MoveNode currentMove;

    // Scroll offsets
    private double xScroll = 0d;
    private double yScroll = 0d;

    // Sizing properties (calculated in updateNodeItemData())
    private int maxBranches = 1;
    private int maxMoves = 1;

    public GameTreeUI(Game game) {
        this.game = game;
        this.game.addListener(this);
        this.currentMove = game.getCurrentMove();
        this.setPrefHeight(200);

        canvas = new GameTreeCanvas(this);
        canvas.setOnScroll(e -> {
            if (!vScroll.isVisible())
                return;
            double delta = e.getDeltaY();
            double value = vScroll.getValue();
            if (delta > 0) {
                value -= delta;
                if (value < vScroll.getMin())
                    value = vScroll.getMin();
            } else {
                value -= delta;
                if (value > vScroll.getMax())
                    value = vScroll.getMax();
            }
            vScroll.setValue(value);
        });
        container = new CanvasContainer(canvas);
        setCenter(container);

        hScroll = new ScrollBar();
        hScroll.getStyleClass().add("scroll-bar-light");
        hScroll.setMin(0);
        hScroll.setOrientation(Orientation.HORIZONTAL);
        hScroll.setManaged(false);
        hScroll.setVisible(false);
        hScroll.valueProperty().addListener(e -> {
            xScroll = hScroll.getValue();
            render();
        });
        BorderPane bottom = new BorderPane();
        bottom.setCenter(hScroll);
        corner = new FlowPane();
        corner.getStyleClass().add("corner");
        corner.setMaxWidth(16);
        bottom.setRight(corner);
        bottom.setMinWidth(0);
        setBottom(bottom);

        vScroll = new ScrollBar();
        vScroll.getStyleClass().add("scroll-bar-light");
        vScroll.setMin(0);
        vScroll.setOrientation(Orientation.VERTICAL);
        vScroll.setManaged(false);
        vScroll.setVisible(false);
        vScroll.valueProperty().addListener(e -> {
            yScroll = vScroll.getValue();
            render();
        });
        setRight(vScroll);

        updateNodeItemData();

        vScroll.setVisible(false);
        vScroll.setManaged(false);
        hScroll.setVisible(false);
        hScroll.setManaged(false);
        corner.setVisible(false);
        corner.setManaged(false);
    }

    /*
        Reconstructs the widget structure and recalculates all positioning.
     */
    private void updateNodeItemData() {
        itemData.clear();
        maxBranches = 1;
        maxMoves = 1;
        indexMoveNode(game.getGameTree(), null,  0);
        updateComponents();
        render();
    }

    private void updateComponents() {
        // Calculate sizing
        double treeWidth = 2 * DRAW_X_MARGIN + maxBranches * MoveNodeItem.DISPLAY_WIDTH;
        double treeHeight = 2 * DRAW_Y_MARGIN + maxMoves * MoveNodeItem.DISPLAY_HEIGHT;
        double componentWidth = getWidth();
        double componentHeight = getHeight();
        // Adjust scrollbar properties depending on component sizing
        boolean vScrollable = componentHeight < treeHeight;
        vScroll.setVisible(vScrollable);
        vScroll.setManaged(vScrollable);
        if (vScrollable) {
            vScroll.setMax(treeHeight - componentHeight + DRAW_X_MARGIN * 2);
            vScroll.setVisibleAmount((componentHeight / treeHeight) * (treeHeight - componentHeight));
        }
        boolean hScrollable = componentWidth < treeWidth;
        hScroll.setVisible(hScrollable);
        hScroll.setManaged(hScrollable);
        if (hScrollable) {
            hScroll.setMax(treeWidth - componentWidth + DRAW_Y_MARGIN * 2);
            hScroll.setVisibleAmount((componentWidth / treeWidth) * (treeWidth - componentWidth));
        }

        corner.setVisible(hScrollable && vScrollable);
        corner.setManaged(hScrollable && vScrollable);
    }

    /**
     * Recursively indexes a given MoveNode and all its child nodes. A corresponding MoveNodeItem
     * object will be created, and its component sizing and position will be calculated.
     *
     * @param node The move node to index.
     * @param itemData Widget data storage.
     * @param indentLevel Branch indentation level used to display child branches.
     * @param chainCounter The counter for the index of the current node in the variation chain.
     */
    protected static final int DRAW_X_MARGIN = 10;
    protected static final int DRAW_Y_MARGIN = 10;
    private void indexMoveNode(MoveNode node, MoveNodeItem parentItem, int row) {
        // Resolve node position collision
        List<MoveNodeItem> itemList = itemData.getOrDefault(node.getMoveNumber(), new ArrayList<>());
        for (MoveNodeItem item : itemList)
            if (item.getDisplayRow() == row) {
                row++;
            }

        if (maxBranches < row)
            maxBranches = row;
        if (maxMoves < node.getMoveNumber())
            maxMoves = node.getMoveNumber();

        double xPos = DRAW_X_MARGIN + row * MoveNodeItem.DISPLAY_WIDTH;
        double yPos = DRAW_Y_MARGIN + node.getMoveNumber() * MoveNodeItem.DISPLAY_HEIGHT;
        MoveNodeItem item = new MoveNodeItem(this, parentItem, xPos, yPos, node);
        itemData.putIfAbsent(node.getMoveNumber(), new ArrayList<>());
        item.setDisplayRow(row);
        itemList = itemData.get(node.getMoveNumber());
        itemList.add(item);
        if (node.hasChildren()) {
            for (MoveNode child : node.getChildren()) {
                indexMoveNode(child, item, node.getChildren().indexOf(child) == 0 ? row : ++row);
            }
        }
    }

    private void render() {
        canvas.render();
    }

    @Override
    public void movePlayed(Stone[] board, int x, int y, int color) {
        updateNodeItemData();
    }

    @Override
    public void currentMoveChanged(MoveNode currentMove) {
        this.currentMove = currentMove;
        updateNodeItemData();
    }

    public Map<Integer, List<MoveNodeItem>> getItemData() {
        return itemData;
    }

    public MoveNode getCurrentMove() {
        return currentMove;
    }

    public double getXOffset() {
        return -xScroll;
    }

    public double getYOffset() {
        return -yScroll;
    }
}
