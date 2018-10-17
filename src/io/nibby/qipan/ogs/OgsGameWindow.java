package io.nibby.qipan.ogs;

import io.nibby.qipan.sound.Sound;
import io.nibby.qipan.ui.board.BoardUI;
import javafx.scene.layout.BorderPane;

/*
    This is the window that displays a single OGS game.
 */
public class OgsGameWindow extends BorderPane {

    private OgsGameData ogsGame;
    private OgsGameController controller;
    private OgsService ogs;
    private BoardUI goban;
    private BorderPane root;

    public OgsGameWindow() {
        root = new BorderPane();
        root.setCenter(goban);
        setCenter(root);
    }

    /**
     * Invoked when the OGSService has established a successful connection to the desired game.
     *
     * @param connection
     */
    public void onConnection(OgsGameData connection) {
        ogsGame = connection;
        controller = new OgsGameController();
        goban = new BoardUI(ogsGame.getGame(), controller);
        root.setCenter(goban);
    }

    /**
     * Invoked when OGSService receives a move played event on the game.
     *
     * @param x x co-ordinate of the move played
     * @param y y co-ordinate of the move played
     * @param time Time in milliseconds for this move to be played since last move.
     */
    public void onMovePlayed(int x, int y, int time) {
        controller.placeMove(x, y, new Sound.ActionCallback() {
            @Override
            public void performAction() {

            }
        });
    }

}
