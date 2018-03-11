package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.BoardMetrics;
import io.nibby.qipan.ui.board.Stone;
import io.nibby.qipan.ui.board.StoneStyle;
import io.nibby.qipan.sound.Sound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Game {

    private int boardWidth, boardHeight;
    private GameRules rules;
    private Stone[] stones;
    private MoveNode gameTree;
    private MoveNode currentMove;

    public Game(int bWidth, int bHeight, GameRules rules) {
        this.boardWidth = bWidth;
        this.boardHeight = bHeight;
        this.rules = rules;
        this.stones = new Stone[boardWidth * boardHeight];

        gameTree = new MoveNode();
        currentMove = gameTree;
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

    /**
     * Places a single stone onto the go board as an independent move.
     *
     * @param x x co-ordinate on the board.
     * @param y y co-ordinate on the board.
     * @param color Color of the stone.
     * @param style Stone styling option, used to determine the wobble margin.
     * @param metrics Sizing information to calculate stone setWobble and placement offset.
     */
    public PlaceMoveResult placeMove(int x, int y, int color, StoneStyle style, BoardMetrics metrics,
                                     Sound.ActionCallback callback) {
        PlaceMoveResult result = new PlaceMoveResult();
        MoveNode resultNode = new MoveNode(currentMove);
        /*
            First check if the move is on the board and on an empty intersection.
            Then check for illegal ko squares.
         */
        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            result.result = PlaceMoveResult.PLACE_ILLEGAL_POSITION;
            return result;
        }
        if (stones[x + y * boardWidth] != null && !rules.allowCollision(stones, x, y, color)) {
            result.result = PlaceMoveResult.PLACE_ILLEGAL_POSITION;
            return result;
        }

        if (x == currentMove.lastKoX && y == currentMove.lastKoY && !rules.allowKoRecapture(stones, x, y, color)) {
            result.result = PlaceMoveResult.PLACE_ILLEGAL_KO;
            return result;
        }

        // Create a hypothetical board position with the new move in place
        Stone[] testPosition = Arrays.copyOf(stones, stones.length);
        testPosition[x + y * boardWidth] = new Stone(color, x, y);

        /*
            Next, check the liberties of opponent's adjacent stones and check if there is a capture
            upon playing this move.

                TODO: The New Zealand ruleset does allow suicidal moves, so perhaps GameRules must be taken into consideration?
         */
        Stone[] adjacentStones = getAdjacentStones(testPosition, x, y, false);
        List<Stone> lastChain = new ArrayList<>();
        int captures = 0;

        for (Stone adjacentStone : adjacentStones) {
            List<Integer> visited = new ArrayList<>();
            boolean isAlly = adjacentStone.getColor() == color;

            // Not checking for suicide here
            if (isAlly)
                continue;

            Object[] resultTuple = getChainProperties(testPosition, adjacentStone.getX(), adjacentStone.getY(), visited,
                    adjacentStone.getColor());
            int liberties = (int) resultTuple[0];
            List<Stone> stoneChain = (List<Stone>) resultTuple[1];
            if (liberties == 0) {
                captures += stoneChain.size();
                for (Stone stone : stoneChain) {
                    int xx = stone.getX();
                    int yy = stone.getY();
                    testPosition[xx + yy * boardWidth] = null;
                }
                lastChain = stoneChain;
            }
        }

        // There will be a capture of 1 stone upon playing this move
        // Check if this is the illegal ko recapture
        if (captures == 1) {
            resultNode.lastKoX = lastChain.get(0).getX();
            resultNode.lastKoY = lastChain.get(0).getY();
        } else {
            // Negative ko co-ordinates means no illegal ko square next turn
            resultNode.lastKoX = -1;
            resultNode.lastKoY = -1;
        }

        /*
            Now we check if the current move is suicidal
         */
        Object[] selfChain = getChainProperties(testPosition, x, y, new ArrayList<Integer>(),
                testPosition[x + y * boardWidth].getColor());
        int selfLiberties = (int) selfChain[0];

        // If this hypothetical move has no liberties, and has made no captures, it is a suicidal move
        if (selfLiberties == 0 && captures == 0) {
            //TODO account for New Zealand rules
            result.result = PlaceMoveResult.PLACE_SUICIDAL;
            return result;
        }

        //Wibbly wobbly
        Stone stone = testPosition[x + y * boardWidth];
        stone.setWobble((Math.random() + 0.1d) * style.wobbleMargin());
        stone.onPlace(metrics);
        stones[x + y * getBoardWidth()] = stone;

        // Nudge effect
        boolean bigCollision = false;
        boolean snap = false;
        List<Stone> wobbles = new ArrayList<>();
        wobbles.add(stone);
        Stone[] adjacent = getAdjacentStones(testPosition, x, y, false);
        for(Stone s : adjacent) {
            if (Math.abs(s.getY() - y) == 1 || (int) (Math.random() * 3) == 1) {
                double wobble = (Math.abs(s.getY() - y) == 1)
                        ? style.wobbleMargin()
                        : (Math.random() + 0.1d) * style.wobbleMargin() / 2;
                if (Math.abs(s.getY() - y) == 1) {
                    stone.setWobble(style.wobbleMargin());
                    snap = true;
                }
                s.setWobble(wobble);
                s.nudge(s.getX() - x, s.getY() - y, metrics);
                wobbles.add(s);

                // Collision detection
                if ((int) (Math.random() * 5) < 2) {
                    Stone[] adjacent2 = getAdjacentStones(testPosition, s.getX(), s.getY(), false);
                    if (adjacent2.length >= 2)
                        bigCollision = true;
                    for (Stone ss : adjacent2) {
                        if (ss.equals(stone) || ss.equals(s))
                            continue;
                        ss.setWobble((Math.random() + 0.1d) * style.wobbleMargin() / 2);
                        ss.nudge(s.getX() - x, s.getY() - y, metrics);
                        wobbles.add(ss);
                    }
                }
            }
        }
        result.wobbleStones = wobbles.toArray(new Stone[wobbles.size()]);
        Sound.playMove(color, adjacent.length, snap, bigCollision, callback);

        resultNode.stones = stones;
        result.node = resultNode;
        currentMove.addChild(resultNode);
        currentMove = resultNode;
        this.stones = Arrays.copyOf(testPosition, testPosition.length);

        return result;
    }

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
    private Object[] getChainProperties(Stone[] board, int x, int y, List<Integer> visited, int color) {
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

    public Stone[] getAdjacentStones(int x, int y, boolean sameColorOnly) {
        return getAdjacentStones(this.stones, x, y, sameColorOnly);
    }

    public Stone[] getAdjacentStones(Stone[] stones, int x, int y, boolean sameColorOnly) {
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
