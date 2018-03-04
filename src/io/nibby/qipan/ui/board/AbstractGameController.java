package io.nibby.qipan.ui.board;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.sound.Sound;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public abstract class AbstractGameController {

    boolean shiftActive = false;
    boolean ctrlActive = false;

    BoardCanvas boardView;
    BoardInputCanvas boardInputView;
    BoardContainer container;
    Game game;

    protected void onAdd(BoardContainer container) {
        this.boardView = container.getBoardView();
        this.boardInputView = container.getBoardInputView();
        this.container = container;
        game = container.getGame();
    }

    public void mouseMoved(int x, int y, int oldX, int oldY) {

    }

    public void mousePressed(int x, int y, int oldX, int oldY, MouseButton button) {

    }

    public void mouseClicked(int x, int y, int oldX, int oldY, MouseButton button) {

    }

    public void mouseDragged(int x, int y, int oldX, int oldY) {

    }

    public void mouseScrolled(double notch) {

    }

    public Game.PlaceMoveResult placeMove(int x, int y, int playerColor, Sound.ActionCallback callback) {
        Game.PlaceMoveResult result = game.placeMove(x, y, playerColor, container.getStoneStyle(),
                container.getMetrics(), callback);
        if (result.wobbleStones != null)
        for (Stone stone : result.wobbleStones)
            container.getBoardInputView().addWobbleStone(stone);
        return result;
    }

    public void keyPressed(KeyCode key) {
        if (key.equals(KeyCode.CONTROL))
            ctrlActive = true;
        if (key.equals(KeyCode.SHIFT))
            shiftActive = true;

    }

    public void keyReleased(KeyCode key) {
        if (key.equals(KeyCode.CONTROL))
            ctrlActive = false;
        if (key.equals(KeyCode.SHIFT))
            shiftActive = false;
    }
}
