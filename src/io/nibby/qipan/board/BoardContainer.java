package io.nibby.qipan.board;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

/*
    A container to organize BoardCanvas + BoardInputCanvas.
 */
public class BoardContainer extends Pane {

    // The sides to place the horizontal and vertical co-ordinates
    public static final int LABEL_ORIENTATION_TOP_LEFT = 0;
    public static final int LABEL_ORIENTATION_TOP_RIGHT = 1;
    public static final int LABEL_ORIENTATION_BOTTOM_LEFT = 2;
    public static final int LABEL_ORIENTATION_BOTTOM_RIGHT = 3;
    public static final int LABEL_ORIENTATION_ALL = 4;

    // The order from which the numerical vertical coordinate column begins
    // ASCENDING = 1 -> 19 (on 19x19)
    // DESCENDING = 19 -> 1 (on 19x19)
    // Normally speaking, most western servers begin the y-coordinate at 1 from top to bottom.
    public static final int LABEL_Y_ASCENDING = 0;
    public static final int LABEL_Y_DESCENDING = 1;

    // Other metrics...
    protected double stoneSize, stoneGap;
    protected double offsetX, offsetY;
    protected double gridOffsetX, gridOffsetY;
    protected double gap;

    protected double drawWidth, drawHeight;
    protected double minSize;
    protected double gridSize;
    protected int labelOrientation = LABEL_ORIENTATION_TOP_RIGHT;
    protected int labelYOrder = LABEL_Y_ASCENDING;
    protected boolean drawLabels = false;

    protected int boardWidth;
    protected int boardHeight;

    protected BoardCanvas boardView;
    protected BoardInputCanvas boardInputView;
    protected BoardController boardController;

    public BoardContainer(int bWidth, int bHeight, BoardController controller) {
        this.boardWidth = bWidth;
        this.boardHeight = bHeight;
        calculateMetrics();

        boardView = new BoardCanvas(this);
        boardInputView = new BoardInputCanvas(this);
        getChildren().addAll(boardView, boardInputView);
        boardInputView.toFront();
        setCursor(Cursor.HAND);

        widthProperty().addListener(e -> {
            updateSize(getWidth(), getHeight());
        });
        heightProperty().addListener(e -> {
            updateSize(getWidth(), getHeight());
        });
    }

    /*
        Calculates the appropriate sizing for board elements.
     */
    private void calculateMetrics() {
        double margin = (!drawLabels) ? 35 : (labelOrientation == LABEL_ORIENTATION_ALL) ? 65 : 50;
        drawWidth = getWidth() - margin;
        drawHeight = getHeight() - margin;
        minSize = Math.min(drawWidth, drawHeight);
        stoneSize = (minSize - 50) / Math.max(boardWidth, boardHeight);
        stoneGap = stoneSize / 12d;
        gridSize = stoneSize + stoneGap;

        // TODO potentially want to shift these according to arrangement of nearby panes
        offsetX = getWidth() / 2 - (boardWidth - 1) * gridSize / 2;
        offsetY = getHeight() / 2 - (boardHeight - 1) * gridSize / 2;
        gridOffsetX = drawLabels ? (labelOrientation == LABEL_ORIENTATION_ALL) ? 40 : 20 : 0;
        gridOffsetY = drawLabels ? (labelOrientation == LABEL_ORIENTATION_ALL) ? 40 : 20 : 0;
        gap = drawLabels ? stoneSize / stoneGap / 11 : 0;

    }

    private void updateSize(double width, double height) {
        super.setPrefSize(width, height);
        boardView.setWidth(width);
        boardView.setHeight(height);
        boardInputView.setWidth(width);
        boardInputView.setHeight(height);

        calculateMetrics();
        render();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();

        boardView.setLayoutX(x);
        boardView.setLayoutY(y);
        boardView.setWidth(w);
        boardView.setHeight(h);
        boardInputView.setLayoutX(x);
        boardInputView.setLayoutY(y);
        boardInputView.setWidth(w);
        boardInputView.setHeight(h);
    }

    private void render() {
        boardView.render();
        boardInputView.render();
    }
}
