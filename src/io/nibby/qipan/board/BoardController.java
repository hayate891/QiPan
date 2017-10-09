package io.nibby.qipan.board;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public abstract class BoardController {

    public static final int MODE_OBSERVE = 0;
    public static final int MODE_PLAY = 1;
    public static final int MODE_REVIEW = 2;

    protected boolean shiftActive = false;
    protected boolean ctrlActive = false;

    public void mouseMoved(int x, int y, int oldX, int oldY) {
        // TODO implement later
    }

    public void mouseClicked(int x, int y, int oldX, int oldY, MouseButton button) {
        // TODO implement later
    }

    public void mouseDragged(int x, int y, int oldX, int oldY) {
        // TODO implement later
    }

    public void mouseScrolled(double notch) {
        // TODO implement later
    }

    public void keyPressed(KeyCode key) {
        // TODO implement later
    }

    public void keyReleased(KeyCode key) {
        // TODO implement later
    }
}
