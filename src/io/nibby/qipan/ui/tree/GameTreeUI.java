package io.nibby.qipan.ui.tree;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameListener;
import io.nibby.qipan.game.MoveNode;
import io.nibby.qipan.ui.CanvasContainer;
import io.nibby.qipan.ui.board.Stone;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;

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
    private Map<Integer, List<MoveNodeItem>> itemData = new HashMap<>();
    private MoveNode currentMove;

    // Scroll offsets
    private double xScroll = 0d;
    private double yScroll = 0d;

    // Sizing properties (calculated in updateNodeItemData())
    private int maxColumns = 1;
    private int maxMoves = 1;

    public GameTreeUI(Game game) {
        this.game = game;
        this.game.addListener(this);
        this.currentMove = game.getCurrentMove();
        this.setPrefWidth(300);

        canvas = new GameTreeCanvas(this);
        container = new CanvasContainer(canvas);
        setCenter(container);

        hScroll = new ScrollBar();
        hScroll.setMin(0);
        hScroll.setOrientation(Orientation.HORIZONTAL);
        hScroll.setManaged(false);
        hScroll.setVisible(false);
        widthProperty().addListener(e -> {
            hScroll.setMaxWidth(getWidth() - 16);
        });
        setBottom(hScroll);

        vScroll = new ScrollBar();
        vScroll.setMin(0);
        vScroll.setOrientation(Orientation.VERTICAL);
        vScroll.setManaged(false);
        vScroll.setVisible(false);
        setRight(vScroll);

        updateNodeItemData();
    }

    /*
        Reconstructs the widget structure and recalculates all positioning.
     */
    private void updateNodeItemData() {
        itemData.clear();
        maxColumns = 1;
        maxMoves = 1;
        indexMoveNode(game.getGameTree(), null,  0, 0);
        updateComponents();
        render();
    }

    private void updateComponents() {
        // Calculate sizing
        double treeWidth = 2 * DRAW_X_MARGIN + maxColumns * MoveNodeItem.DISPLAY_WIDTH;
        double treeHeight = 2 * DRAW_Y_MARGIN + maxMoves * MoveNodeItem.DISPLAY_HEIGHT;
        double componentWidth = getWidth();
        double componentHeight = getHeight();

        // Adjust scrollbar properties depending on component sizing
        boolean vScrollable = componentHeight < treeHeight;
        vScroll.setVisible(vScrollable);
        vScroll.setManaged(vScrollable);
        if (vScrollable) {
            vScroll.setMax(treeHeight);
            vScroll.setVisibleAmount(componentHeight);
        }

        boolean hScrollable = componentWidth < treeWidth;
        hScroll.setVisible(hScrollable);
        hScroll.setManaged(hScrollable);
        if (hScrollable) {
            hScroll.setMax(treeWidth);
            hScroll.setVisibleAmount(componentWidth);
        }
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
    private static final int DRAW_X_MARGIN = 10;
    private static final int DRAW_Y_MARGIN = 10;
    private void indexMoveNode(MoveNode node, MoveNodeItem parentItem, int column, int chainCounter) {
        // Resolve node position collision
        List<MoveNodeItem> itemList = itemData.getOrDefault(node.getMoveNumber(), new ArrayList<>());
        for (MoveNodeItem item : itemList)
            if (item.getDisplayColumn() == column)
                column++;

        if (maxColumns < column)
            maxColumns = column;
        if (maxMoves < node.getMoveNumber())
            maxMoves = node.getMoveNumber();

        double xPos = DRAW_X_MARGIN + column * MoveNodeItem.DISPLAY_WIDTH;
        double yPos = DRAW_Y_MARGIN + node.moveNumber * MoveNodeItem.DISPLAY_HEIGHT;
        MoveNodeItem item = new MoveNodeItem(this, parentItem, xPos, yPos, node);
        itemData.putIfAbsent(node.getMoveNumber(), new ArrayList<>());
        itemList = itemData.get(node.getMoveNumber());
        itemList.add(item);
        if (node.hasChildren()) {
            for (MoveNode child : node.getChildren()) {
                indexMoveNode(child, item, node.getChildren().indexOf(child) == 0 ? column : ++column, chainCounter + 1);
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
}
