package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.Stone;

/*
 * Process specific game elements differently depending on the rules used.
 */
public enum GameRules {

    CHINESE {
        @Override
        public boolean allowKoRecapture(Stone[] board, int x, int y, int color) {
            return false;
        }

        @Override
        public boolean allowSuicide(Stone[] board, int x, int y, int color) {
            return false;
        }

        @Override
        public boolean allowCollision(Stone[] board, int x, int y, int color) {
            return false;
        }

        @Override
        public void processSuicide(Stone[] board, Stone[] deadChain) {}

        @Override
        public void scoreGame(Stone[] boardPosition) {

        }
    };

    /**
     *
     * @param board The current board position
     * @param x The x co-ordinate of the new move
     * @param y The y co-ordinate of the new move
     * @param color Color of the stone, see Stone.java
     * @return Whether or not an immediate ko recapture is permitted in the game.
     */
    public abstract boolean allowKoRecapture(Stone[] board, int x, int y, int color);

    /**
     *
     * @param board The current board position
     * @param x The x co-ordinate of the new move
     * @param y The y co-ordinate of the new move
     * @param color Color of the stone, see Stone.java
     * @return Whether or not suicidal moves are permitted in the game.
     */
    public abstract boolean allowSuicide(Stone[] board, int x, int y, int color);

    /**
     *
     * @param board The current board position
     * @param x The x co-ordinate of the new move
     * @param y The y co-ordinate of the new move
     * @param color Color of the stone, see Stone.java
     * @return Whether or not stones are allowed to be placed on a point where another stone is present.
     */
    public abstract boolean allowCollision(Stone[] board, int x, int y, int color);

    /**
     * Handles the action of executing a suicidal move.
     *
     * @param board The current board position
     * @param deadChain The suicidal chain of stones
     */
    public abstract void processSuicide(Stone[] board, Stone[] deadChain);

    /**
     * Scores the game.
     *
     * @param boardPosition The current board position.
     */
    public abstract void scoreGame(Stone[] boardPosition);
}
