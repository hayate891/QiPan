package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.Stone;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class AbstractRules {

    // A data tuple for returning information related to stone placement.
    public static class PlaceMoveResult {

        public static final int PLACE_OK = 0;
        public static final int PLACE_SUICIDAL = 1;
        public static final int PLACE_ILLEGAL_KO = 2;
        public static final int PLACE_ILLEGAL_POSITION = 3;
        public static final int PLACE_INDETERMINATE = -1;

        public Stone[] wobbleStones;
        public int result;
        public MoveNode node;
    }

    public abstract PlaceMoveResult playMove(MoveNode currentMove, int x, int y, int player, int boardWidth, int boardHeight);

    /**
     * Returns attributes pertaining to the adjacent stone chain.
     *
     * @param board The board position to evaluate
     * @param x x position of the origin
     * @param y y position of the origin
     * @param visited A list of already visited points
     * @param color The color of the stone to check, black or white.
     * @return An object tuple of size 2: liberty as integer, and an array of Stones in the chain.
     */
    protected Object[] getChainProperties(Stone[] board, int x, int y, List<Integer> visited, int color, int boardWidth, int boardHeight) {
        List<Stone> stoneChain = new ArrayList<>();
        Stack<Stone> toVisit = new Stack<>();
        int liberties = 0;
        Stone currentStone = board[x + y * boardWidth];
        if (!visited.contains(x + y * boardWidth)) {
            toVisit.push(currentStone);
            stoneChain.add(currentStone);
        }

        while (toVisit.size() > 0) {
            Stone visitor = toVisit.pop();
            int px = visitor.getX();
            int py = visitor.getY();
            int left = py * boardWidth + px - 1;
            int right = py * boardWidth + px + 1;
            int up = (py - 1) * boardWidth + px;
            int down = (py + 1) * boardWidth + px;

            if (px > 0 && !visited.contains(left)) {
                if (board[left] == null)
                    liberties++;
                else if (board[left].getColor() == color) {
                    toVisit.add(board[left]);
                    stoneChain.add(board[left]);
                }
                visited.add(left);
            }
            if (px < boardWidth - 1 && !visited.contains(right)) {
                if (board[right] == null)
                    liberties++;
                else if (board[right].getColor() == color) {
                    toVisit.add(board[right]);
                    stoneChain.add(board[right]);
                }
            }
            if (py > 0 && !visited.contains(up)) {
                if (board[up] == null)
                    liberties++;
                else if (board[up].getColor() == color) {
                    toVisit.add(board[up]);
                    stoneChain.add(board[up]);
                }
                visited.add(up);
            }
            if (py < boardHeight - 1 && !visited.contains(down)) {
                if (board[down] == null)
                    liberties++;
                else if (board[down].getColor() == color) {
                    toVisit.add(board[down]);
                    stoneChain.add(board[down]);
                }
                visited.add(down);
            }
        }

        return new Object[] {
                liberties,  // int
                stoneChain  // List<Stone>
        };
    }

    public Stone[] getAdjacentStones(Stone[] stones, int x, int y, boolean sameColorOnly, int boardWidth, int boardHeight) {
        List<Stone> result = new ArrayList<>();
        Stone origin = stones[x + y * boardWidth];
        // left
        if (x > 0 && stones[(x - 1) + y * boardWidth] != null)
            result.add(stones[(x - 1) + y * boardWidth]);
        // right
        if (x < boardWidth - 1 && stones[(x + 1) + y * boardWidth] != null)
            result.add(stones[(x + 1) + y * boardWidth]);
        // top
        if (y > 0 && stones[x + (y - 1) * boardWidth] != null)
            result.add(stones[x + (y - 1) * boardWidth]);
        // bottom
        if (y < boardHeight - 1 && stones[x + (y + 1) * boardWidth] != null)
            result.add(stones[x + (y + 1) * boardHeight]);
        if (sameColorOnly)
            for (int i = 0; i < result.size();)
                if (origin.getColor() != result.get(i).getColor())
                    result.remove(i);
                else
                    i++;

        return result.toArray(new Stone[result.size()]);
    }
}
