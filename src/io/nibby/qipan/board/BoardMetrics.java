package io.nibby.qipan.board;

import io.nibby.qipan.game.Game;

public class BoardMetrics {

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

    double stoneSize, stoneGap;
    double offsetX, offsetY;
    double gridOffsetX, gridOffsetY;
    double gap;
    double drawWidth, drawHeight;
    double minSize;
    double gridSize;
    int boardWidth;
    int boardHeight;
    boolean drawLabels = false;
    int labelOrientation = LABEL_ORIENTATION_TOP_RIGHT;
    int labelYOrder = LABEL_Y_ASCENDING;

    public void recalculate(BoardContainer container) {
        double margin = (!drawLabels) ? 35 : (labelOrientation == LABEL_ORIENTATION_ALL) ? 65 : 50;
        Game game = container.game;
        this.boardWidth = game.getBoardWidth();
        this.boardHeight = game.getBoardHeight();
        drawWidth = container.getWidth() - margin;
        drawHeight = container.getHeight() - margin;
        minSize = Math.min(drawWidth, drawHeight);
        stoneSize = (minSize - 50) / Math.max(boardWidth, boardHeight);
        stoneGap = stoneSize / 12d;
        gridSize = stoneSize + stoneGap;

        // TODO potentially want to shift these according to arrangement of nearby panes
        offsetX = container.getWidth() / 2 - (boardWidth - 1) * gridSize / 2;
        offsetY = container.getHeight() / 2 - (boardHeight - 1) * gridSize / 2;
        gridOffsetX = drawLabels ? (labelOrientation == LABEL_ORIENTATION_ALL) ? 40 : 20 : 0;
        gridOffsetY = drawLabels ? (labelOrientation == LABEL_ORIENTATION_ALL) ? 40 : 20 : 0;
        gap = drawLabels ? stoneSize / stoneGap / 11 : 0;
    }

    /**
     * Returns the x value on-screen that corresponds to the given x co-ordinate on the board.
     *
     * @param x The x co-ordinate on the board.
     * @return
     */
    public double getGridX(int x) {
        return gridOffsetX + offsetX + x * (gridSize - gap);
    }

    public double getBoardStoneX(int x) {
        return getGridX(x) - gridSize / 2 + gap * 2;
    }

    /**
     * Returns the y value on-screen that corresponds to the given y co-ordinate on the board.
     *
     * @param y The y co-ordinate on the board.
     * @return
     */
    public double getGridY(int y) {
        return gridOffsetY + offsetY + y * (gridSize);
    }

    public double getBoardStoneY(int y) {
        return getGridY(y) - gridSize / 2 + gap * 2;
    }

    // Getters

    public double getStoneSize() {
        return stoneSize;
    }

    public double getStoneGap() {
        return stoneGap;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public double getGridOffsetX() {
        return gridOffsetX;
    }

    public double getGridOffsetY() {
        return gridOffsetY;
    }

    public double getGap() {
        return gap;
    }

    public double getDrawWidth() {
        return drawWidth;
    }

    public double getDrawHeight() {
        return drawHeight;
    }

    public double getMinSize() {
        return minSize;
    }

    public double getGridSize() {
        return gridSize;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public boolean isDrawLabels() {
        return drawLabels;
    }

    public int getLabelOrientation() {
        return labelOrientation;
    }

    public int getLabelYOrder() {
        return labelYOrder;
    }
}
