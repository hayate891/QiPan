package io.nibby.qipan.board;

import io.nibby.qipan.game.Game;
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
