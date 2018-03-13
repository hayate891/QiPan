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
    private MoveNode currentMove = null;

    // Scroll offsets
    private double xScroll = 0d;
    private double yScroll = 0d;

    // Display constants
    public static final int ITEM_MARGIN_HORIZONTAL = 5;
    public static final int ITEM_MARGIN_VERTICAL = 5;
    public static final int DISPLAY_FULL = 0;
    public static final int DISPLAY_SMALL = 1;
    private int displayMode = DISPLAY_SMALL;

    public GameTreeUI(Game game) {
        this.game = game;
        this.game.addListener(this);
        this.setPrefWidth(300);

        canvas = new GameTreeCanvas(this);
        container = new CanvasContainer(canvas);
        setCenter(container);

        hScroll = new ScrollBar();
        hScroll.setOrientation(Orientation.HORIZONTAL);
        hScroll.setManaged(false);
        hScroll.setVisible(false);
        widthProperty().addListener(e -> {
            hScroll.setMaxWidth(getWidth() - 16);
        });
        setBottom(hScroll);

        vScroll = new ScrollBar();
        vScroll.setOrientation(Orientation.VERTICAL);
        vScroll.setManaged(false);
        vScroll.setVisible(false);
        setRight(vScroll);
    }

    /*
        Updates the tree data
     */
    private void update() {
        render();
    }

    /*
        Reconstructs the widget structure and recalculates all positioning.
     */
    private void reindexMoveNodes() {
        itemData.clear();
        indexMoveNode(game.getGameTree(), null,  0, 0);
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
    private static final int ITEM_X_OFFSET = 10;
    private static final int ITEM_Y_OFFSET = 10;
    private void indexMoveNode(MoveNode node, MoveNodeItem parentItem, int column, int chainCounter) {
        double itemWidth = (displayMode == DISPLAY_FULL
                ? MoveNodeItem.DISPLAY_FULL_WIDTH : MoveNodeItem.DISPLAY_SMALL_WIDTH) + ITEM_MARGIN_HORIZONTAL;
        double itemHeight = (displayMode == DISPLAY_FULL
                ? MoveNodeItem.DISPLAY_FULL_HEIGHT : MoveNodeItem.DISPLAY_SMALL_HEIGHT) + ITEM_MARGIN_VERTICAL;

        // Resolve node position collision
        List<MoveNodeItem> itemList = itemData.getOrDefault(node.getMoveNumber(), new ArrayList<>());
        for (MoveNodeItem item : itemList)
            if (item.getDisplayColumn() == column)
                column++;

        double xPos = ITEM_X_OFFSET + column * itemWidth;
        double yPos = ITEM_Y_OFFSET + node.moveNumber * itemHeight;
        MoveNodeItem item = new MoveNodeItem(this, parentItem, xPos, yPos, node);
        item.setDisplayColumn(column);
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
        reindexMoveNodes();
        update();
    }

    @Override
    public void currentMoveChanged(MoveNode currentMove) {
        this.currentMove = currentMove;
        reindexMoveNodes();
        update();
    }

    public Map<Integer, List<MoveNodeItem>> getItemData() {
        return itemData;
    }

    public int getDisplayMode() {
        return displayMode;
    }

    public MoveNode getCurrentMove() {
        return currentMove;
    }
}
