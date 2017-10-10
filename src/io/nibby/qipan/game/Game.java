package io.nibby.qipan.game;

import io.nibby.qipan.board.BoardMetrics;
import io.nibby.qipan.board.Stone;

public class Game {

    private int boardWidth, boardHeight;
    private Stone[] stones;

    public Game(int bWidth, int bHeight) {
        this.boardWidth = bWidth;
        this.boardHeight = bHeight;
        this.stones = new Stone[boardWidth * boardHeight];
    }

    public Stone[] getStones() {
        return stones;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Places a single stone onto the go board.
     * The style of the stone is determined here.
     *
     * @param x x co-ordinate on the board.
     * @param y y co-ordinate on the board.
     * @param color Color of the stone.
     * @param newPosition Whether or not a new node should be created for this action.
     *                    'false' for helper stonse, 'true' for a significant game move.
     * @param metrics Sizing information to calculate stone wobble and placement offset.
     */
    public void placeStone(int x, int y, int color, boolean newPosition, BoardMetrics metrics) {
        //TODO temporary code
        Stone stone = new Stone(color, x, y);
        double drawX = metrics.getBoardStoneX(x);
        double drawY = metrics.getBoardStoneY(y);
        stone.setDrawX(drawX);
        stone.setDrawY(drawY);
        stones[x + y * getBoardWidth()] = stone;
    }

    public Stone[] getAdjacentStones(int x, int y, boolean sameColorOnly, boolean includeDiagonals) {
        // TODO implement later
        return null;
    }
}
