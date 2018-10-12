package io.nibby.qipan.ui;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameListener;
import io.nibby.qipan.game.MoveNode;
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
    private MoveNode currentMove;
    private GameTreeNode currentTreeNode;

    // Scroll offsets
    private double xScroll = 0d;
    private double yScroll = 0d;

    private Map<Integer, List<Integer[]>> columnData = new HashMap<>();
    private Map<Integer, List<GameTreeNode>> nodeData = new HashMap<>();
    private int maxColumns = 0;
    private int maxMoves = 0;

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

        reloadTree();

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
    private void reloadTree() {
        nodeData.clear();
        columnData.clear();
        createMoveBranch(game.getGameTreeRoot(), null, 0);
        updateComponents();
        render();
    }

    private void updateComponents() {
        // Calculate sizing
        double tWidth = 2 * DRAW_X_MARGIN + maxColumns * GameTreeNode.DISPLAY_WIDTH;
        double tHeight = 2 * DRAW_Y_MARGIN + maxMoves * GameTreeNode.DISPLAY_HEIGHT;
        double componentWidth = getWidth();
        double componentHeight = getHeight();
        // Adjust scrollbar properties depending on component sizing
        boolean vScrollable = componentHeight < tHeight;
        vScroll.setVisible(vScrollable);
        vScroll.setManaged(vScrollable);
        if (vScrollable) {
            vScroll.setMax(tHeight - componentHeight + DRAW_X_MARGIN * 2 + 16);
            vScroll.setVisibleAmount((componentHeight / tHeight) * (tHeight - componentHeight));
        }
        boolean hScrollable = componentWidth < tWidth;
        hScroll.setVisible(hScrollable);
        hScroll.setManaged(hScrollable);
        if (hScrollable) {
            hScroll.setMax(tWidth - componentWidth + DRAW_Y_MARGIN * 2 + 16);
            hScroll.setVisibleAmount((componentWidth / tWidth) * (tWidth - componentWidth));
        }

        corner.setVisible(hScrollable && vScrollable);
        corner.setManaged(hScrollable && vScrollable);
    }

    protected static final int DRAW_X_MARGIN = 10;
    protected static final int DRAW_Y_MARGIN = 10;

    /**
     * Recursively constructs an entire MoveNode branch as GameTreeNode branch. This method
     * also handles branch collision detection.
     *
     * @param branchRoot The root node of the current branch.
     * @param parent Parent GameTreeNode of the current root node.
     * @param column The intended column the current branch reside (on the UI).
     */
    private void createMoveBranch(MoveNode branchRoot, GameTreeNode parent, int column) {
        if (branchRoot.getParent() == null) {
            // This is the root node, initialize
            columnData.putIfAbsent(0, new ArrayList<>());
            branchRoot.setState(MoveNode.STATE_ROOT);
        }
        MoveNode current = branchRoot;
        int size = 0;
        // The branch start is 1 less than the move number to account for collision with the branch outline
        // on the tree UI.
        int start = branchRoot.getMoveNumber() - 1;
        List<GameTreeNode> branchNodes = new ArrayList<>();
        Stack<MoveNode> childNodes = new Stack<>();
        Map<MoveNode, GameTreeNode> parentNodes = new HashMap<>();
        GameTreeNode currentParent = parent;
        boolean hasChild = false;
        do {
            int moveNumber = current.getMoveNumber();
            double x = DRAW_X_MARGIN + column * GameTreeNode.DISPLAY_WIDTH;
            double y = DRAW_Y_MARGIN + (moveNumber - 1) * GameTreeNode.DISPLAY_HEIGHT;
            GameTreeNode treeNode = new GameTreeNode(this, currentParent, x, y, current);
            nodeData.putIfAbsent(moveNumber, new ArrayList<>());
            nodeData.get(moveNumber).add(treeNode);
            branchNodes.add(treeNode);
            if (current.hasChildren() && current.getChildren().size() > 1) {
                for (MoveNode child : current.getChildren()) {
                    // Ignore the main-variation child because it's already accounted for
                    if (current.getChildren().indexOf(child) == 0)
                        continue;
                    childNodes.push(child);
                    parentNodes.put(child, treeNode);
                }
            }
            currentParent = treeNode;
            size++;
            hasChild = false;
            if (current.equals(currentMove)) {
                currentTreeNode = treeNode;
            }
            if (current.hasChildren()) {
                current = current.getChildren().get(0);
                hasChild = true;
            }
        } while(hasChild);
        int end = start + size;
        int col = column;
        while (!isSegmentAvailable(col, start, end))
            col++;

        columnData.putIfAbsent(col, new ArrayList<>());
        columnData.get(col).add(new Integer[] { start, end });
        if (maxColumns < col)
            maxColumns = col;
        if (maxMoves < start + size)
            maxMoves = start + size;

        for (GameTreeNode node : branchNodes) {
            node.setDisplayColumn(col);
        }

        // Handle child nodes
        while (!childNodes.isEmpty()) {
            MoveNode node = childNodes.pop();
            int nthChild = node.getParent().getChildren().indexOf(node);
            createMoveBranch(node, parentNodes.get(node), parentNodes.get(node).getDisplayColumn() + nthChild);
        }
    }

    private void render() {
        canvas.render();
    }

    @Override
    public void movePlayed(Stone[] board, int x, int y, int color) {
        reloadTree();
    }

    @Override
    public void currentMoveChanged(MoveNode currentMove) {
        this.currentMove = currentMove;
        render();
    }

    private boolean isSegmentAvailable(int column, int start, int end) {
        List<Integer[]> segmentData = columnData.get(column);
        if (segmentData == null) {
            columnData.put(column, new ArrayList<>());
            return true;
        }

        // Avoid stepping into a column where an existing branch line extends further out
        List<Integer[]> nextColumn;
        int c = column;
        while ((nextColumn = columnData.get(c + 1)) != null) {
            for (Integer[] segment : nextColumn) {
                if (start <= segment[0] && end >= segment[0] ||
                        start <= segment[1] && end >= segment[1] ||
                        start >= segment[0] && end <= segment[1])
                    return false;

            }
            c++;
        }

        for (Integer[] segment : segmentData) {
            if (start <= segment[0] && end >= segment[0] ||
                start <= segment[1] && end >= segment[1] ||
                start >= segment[0] && end <= segment[1])
                return false;

        }
        return true;
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

    public Map<Integer, List<GameTreeNode>> getNodeData() {
        return nodeData;
    }
}
