package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.Stone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JapaneseRules extends AbstractRules {

    public PlaceMoveResult playMove(MoveNode currentMove, int x, int y, int boardWidth, int boardHeight, int firstMove) {
        PlaceMoveResult result = new PlaceMoveResult();
        MoveNode resultNode = new MoveNode(currentMove);
        int oddMove = firstMove == Stone.BLACK ? Stone.BLACK : Stone.WHITE;
        int evenMove = firstMove == Stone.BLACK ? Stone.WHITE : Stone.BLACK;
        int color = currentMove.getMoveNumber() % 2 == 1 ? oddMove : evenMove;
        result.color = color;
        resultNode.setState(color == Stone.BLACK ? MoveNode.STATE_MOVE_BLACK : MoveNode.STATE_MOVE_WHITE);
        /*
            First check if the move is on the board and on an empty intersection.
            Then check for illegal ko squares.
         */
        if (x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
            result.result = PlaceMoveResult.PLACE_ILLEGAL_POSITION;
            return result;
        }
        // Another stone already exists here
        if (currentMove.getBoardPosition()[x + y * boardWidth] != null) {
            result.result = PlaceMoveResult.PLACE_ILLEGAL_POSITION;
            return result;
        }
        // Cannot recapture ko immediately
        if (x == currentMove.getLastKoX() && y == currentMove.getLastKoY()) {
            result.result = PlaceMoveResult.PLACE_ILLEGAL_KO;
            return result;
        }
        // Create a hypothetical board position with the new move in place
        Stone[] testPosition = Arrays.copyOf(currentMove.getBoardPosition(), currentMove.getBoardPosition().length);
        testPosition[x + y * boardWidth] = new Stone(color, x, y);
        /*
            Next, check the liberties of opponent's adjacent currentMove.stones and check if there is a capture
            upon playing this move.

                TODO: The New Zealand ruleset does allow suicidal moves, so perhaps AbstractRules must be taken into consideration?
         */
        Stone[] adjacentStones = getAdjacentStones(testPosition, x, y, false, boardWidth, boardHeight);
        List<Stone> lastChain = new ArrayList<>();
        int captures = 0;

        for (Stone adjacentStone : adjacentStones) {
            List<Integer> visited = new ArrayList<>();
            boolean isAlly = adjacentStone.getColor() == color;

            // Not checking for suicide here
            if (isAlly)
                continue;

            Object[] resultTuple = getChainProperties(testPosition, adjacentStone.getX(), adjacentStone.getY(), visited,
                    adjacentStone.getColor(), boardWidth, boardHeight);
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
            resultNode.setLastKoX(lastChain.get(0).getX());
            resultNode.setLastKoY(lastChain.get(0).getY());
        } else {
            // Negative ko co-ordinates means no illegal ko square next turn
            resultNode.setLastKoX(-1);
            resultNode.setLastKoY(-1);
        }
        /*
            Now we check if the current move is suicidal
         */
        Object[] selfChain = getChainProperties(testPosition, x, y, new ArrayList<>(),
                testPosition[x + y * boardWidth].getColor(), boardWidth, boardHeight);
        int selfLiberties = (int) selfChain[0];
        // If this hypothetical move has no liberties, and has made no captures, it is a suicidal move
        // Japanese rules do not allow suicidal moves
        if (selfLiberties == 0 && captures == 0) {
            result.result = PlaceMoveResult.PLACE_SUICIDAL;
            return result;
        }

        result.result = PlaceMoveResult.PLACE_OK;
        Stone stone = testPosition[x + y * boardWidth];
        testPosition[x + y * boardWidth] = stone;
        resultNode.setBoardPosition(Arrays.copyOf(testPosition, testPosition.length));
        result.node = resultNode;
        result.node.setMoveX(x);
        result.node.setMoveY(y);

        return result;
    }
}
