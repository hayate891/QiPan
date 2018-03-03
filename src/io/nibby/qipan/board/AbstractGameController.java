package io.nibby.qipan.board;

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
        this.boardView = container.boardView;
        this.boardInputView = container.boardInputView;
        this.container = container;
        game = container.game;
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

    public Game.PlaceStoneResult placeStone(int x, int y, int playerColor, boolean newPosition, Sound.ActionCallback callback) {
        Game.PlaceStoneResult result = game.placeStone(x, y, playerColor, newPosition, container.metrics, callback);
        for (Stone stone : result.wobbleStones)
            container.boardInputView.addWobbleStone(stone);
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
