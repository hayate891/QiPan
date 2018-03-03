package io.nibby.qipan.game;

import io.nibby.qipan.board.BoardMetrics;
import io.nibby.qipan.board.Stone;
import io.nibby.qipan.board.StoneStyle;
import io.nibby.qipan.sound.Sound;

import java.util.ArrayList;
import java.util.List;

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

    // A data tuple for returning information related to stone placement.
    public static class PlaceStoneResult {

        public static final int PLACE_OK = 0;
        public static final int PLACE_ILLEGAL_KO = 1;
        public static final int PLACE_ILLEGAL_COLLISION = 2;
        public static final int PLACE_INDETERMINATE = -1;

        public Stone[] wobbleStones;
        public int result;
    }

    /**
     * Places a single stone onto the go board.
     * The style of the stone is determined here.
     *
     * @param x x co-ordinate on the board.
     * @param y y co-ordinate on the board.
     * @param color Color of the stone.
     * @param newPosition Whether or not a new node should be created for this action.
     *                    'false' for helper stones, 'true' for a significant game move.
     * @param metrics Sizing information to calculate stone setWobble and placement offset.
     */
    public PlaceStoneResult placeStone(int x, int y, int color, boolean newPosition, BoardMetrics metrics, Sound.ActionCallback callback) {
        //TODO temporary code
        Stone stone = new Stone(color, x, y);
        stone.setWobble((Math.random() + 0.1d) * StoneStyle.CERAMIC.wobbleMargin());
        stone.onPlace(metrics);
        stones[x + y * getBoardWidth()] = stone;

        //TODO again, temporary
        PlaceStoneResult result = new PlaceStoneResult();
        result.result = PlaceStoneResult.PLACE_OK;

        // Nudge effect
        boolean bigCollision = false;
        boolean snap = false;
        List<Stone> wobbles = new ArrayList<>();
        wobbles.add(stone);
        Stone[] adjacent = getAdjacentStones(x, y, false);
        for(Stone s : adjacent) {
            if (Math.abs(s.getY() - y) == 1 || (int) (Math.random() * 3) == 1) {
                double wobble = (Math.abs(s.getY() - y) == 1)
                        ? StoneStyle.CERAMIC.wobbleMargin()
                        : (Math.random() + 0.1d) * StoneStyle.CERAMIC.wobbleMargin() / 2;
                if (Math.abs(s.getY() - y) == 1) {
                    stone.setWobble(StoneStyle.CERAMIC.wobbleMargin());
                    snap = true;
                }
                s.setWobble(wobble);
                s.nudge(s.getX() - x, s.getY() - y, metrics);
                wobbles.add(s);

                // Collision detection
                if ((int) (Math.random() * 5) < 2) {
                    Stone[] adjacent2 = getAdjacentStones(s.getX(), s.getY(), false);
                    if (adjacent2.length >= 2)
                        bigCollision = true;
                    for (Stone ss : adjacent2) {
                        if (ss.equals(stone) || ss.equals(s))
                            continue;
                        ss.setWobble((Math.random() + 0.1d) * StoneStyle.CERAMIC.wobbleMargin() / 2);
                        ss.nudge(s.getX() - x, s.getY() - y, metrics);
                        wobbles.add(ss);
                    }
                }
            }
        }
        result.wobbleStones = wobbles.toArray(new Stone[wobbles.size()]);
        Sound.playMove(color, adjacent.length, snap, bigCollision, callback);
        return result;
    }

    public Stone[] getAdjacentStones(int x, int y, boolean sameColorOnly) {
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
