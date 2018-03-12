package io.nibby.qipan.game;

import io.nibby.qipan.ui.board.Stone;

public interface GameListener {

    void movePlayed(Stone[] board, int x, int y, int color);
    void currentMoveChanged(MoveNode currentMove);

}
