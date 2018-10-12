package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.BoardMetrics;
import io.nibby.qipan.ui.board.Stone;
import io.nibby.qipan.ui.board.StoneStyle;
import io.nibby.qipan.sound.Sound;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<GameListener> listeners = new ArrayList<>();
    private int boardWidth, boardHeight;
    private AbstractRules rules;
    private MoveNode gameTreeRoot;
    private MoveNode currentMove;

    public Game(int bWidth, int bHeight, AbstractRules rules) {
        this.boardWidth = bWidth;
        this.boardHeight = bHeight;
        this.rules = rules;
        // Create the root node
        gameTreeRoot = new MoveNode();
        gameTreeRoot.setBoardPosition(new Stone[boardWidth * boardHeight]);
        gameTreeRoot.setState(MoveNode.STATE_ROOT);
        setCurrentMove(gameTreeRoot);
    }

    public Stone[] getStones() {
        return currentMove.getBoardPosition();
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     *
     * @return The root node of the game tree
     */
    public MoveNode getGameTreeRoot() {
        return gameTreeRoot;
    }

    /**
     * Places a single stone onto the go board as an independent move. The main rules validation is done in the
     * GameRules object associated with the game.
     *
     * @param x x co-ordinate on the board.
     * @param y y co-ordinate on the board.
     * @param style Stone styling option, used to determine the wobble margin.
     * @param metrics Sizing information to calculate stone setWobble and placement offset.
     */
    public AbstractRules.PlaceMoveResult playMove(int x, int y, StoneStyle style, BoardMetrics metrics,
                                                  Sound.ActionCallback callback) {

        AbstractRules.PlaceMoveResult result = rules.playMove(currentMove, x, y, boardWidth, boardHeight);
        if (result.result == AbstractRules.PlaceMoveResult.PLACE_OK) {
            int color = result.color;
            Stone[] position = result.node.getBoardPosition();
            //Wibbly wobbly
            Stone stone = position[x + y * boardWidth];
            stone.setWobble((Math.random() + 0.1d) * style.wobbleMargin());
            stone.onPlace(metrics);
            // Nudge effect
            boolean bigCollision = false;
            boolean snap = false;
            List<Stone> wobbles = new ArrayList<>();
            wobbles.add(stone);
            Stone[] adjacent = getAdjacentStones(position, x, y, false);
            for (Stone s : adjacent) {
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
                        Stone[] adjacent2 = getAdjacentStones(position, s.getX(), s.getY(), false);
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
            currentMove.addChild(result.node);
            setCurrentMove(result.node);
            Sound.playMove(color, adjacent.length, snap, bigCollision, callback);
            fireMovePlayedEvent(x, y, color);
        }
        return result;
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

    public MoveNode getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(MoveNode currentMove) {
        this.currentMove = currentMove;
        fireCurrentMoveChangedEvent(this.currentMove);
    }

    private void fireMovePlayedEvent(int x, int y, int color) {
        for (GameListener l : listeners)
            l.movePlayed(currentMove.getBoardPosition(), x, y, color);
    }

    private void fireCurrentMoveChangedEvent(MoveNode newMove) {
        for (GameListener l : listeners)
            l.currentMoveChanged(newMove);
    }

    public void addListener(GameListener l) {
        listeners.add(l);
    }

    public void removeMoveListener(GameListener l) {
        listeners.remove(l);
    }
}
