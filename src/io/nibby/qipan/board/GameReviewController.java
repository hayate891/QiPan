package io.nibby.qipan.board;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/*
    The standard (default) controller used to edit game data.
 */
public class GameReviewController extends AbstractGameController {

    private int nextColor = Stone.BLACK;

    @Override
    public void mouseMoved(int x, int y, int oldX, int oldY) {
        super.mouseMoved(x, y, oldX, oldY);
    }

    @Override
    public void mousePressed(int x, int y, int oldX, int oldY, MouseButton button) {
        super.mousePressed(x, y, oldX, oldY, button);

    }

    @Override
    public void mouseClicked(int x, int y, int oldX, int oldY, MouseButton button) {
        super.mouseClicked(x, y, oldX, oldY, button);

        // TODO temporary
        if (button.equals(MouseButton.PRIMARY) && x >= 0 && y >= 0) {
            game.placeStone(x, y, nextColor, true, container.metrics);
            nextColor = nextColor == Stone.BLACK ? Stone.WHITE : Stone.BLACK;
            container.render();
        }
    }

    @Override
    public void mouseDragged(int x, int y, int oldX, int oldY) {
        super.mouseDragged(x, y, oldX, oldY);
    }

    @Override
    public void mouseScrolled(double notch) {
        super.mouseScrolled(notch);
    }

    @Override
    public void keyPressed(KeyCode key) {
        super.keyPressed(key);
    }

    @Override
    public void keyReleased(KeyCode key) {
        super.keyReleased(key);
    }
}
