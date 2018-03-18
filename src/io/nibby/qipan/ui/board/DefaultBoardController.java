package io.nibby.qipan.ui.board;

import io.nibby.qipan.game.MoveNode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

/*
    The standard (default) controller used to edit game data.
 */
public class DefaultBoardController extends AbstractGameController {

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
            placeMove(x, y, game.getCurrentMove().getNextColor(), null);
        }
    }

    @Override
    public void mouseDragged(int x, int y, int oldX, int oldY) {
        super.mouseDragged(x, y, oldX, oldY);
    }

    @Override
    public void mouseScrolled(double notch) {
        super.mouseScrolled(notch);
        if (notch > 0)
            gotoPreviousMove();
        else if(notch < 0)
            gotoNextMove();
    }

    @Override
    public void keyPressed(KeyCode key) {
        super.keyPressed(key);

        // TODO Make these configurable
        if (key.equals(KeyCode.LEFT)) {
            gotoPreviousMove();
        }

        if (key.equals(KeyCode.RIGHT)) {
            gotoNextMove();
        }
    }

    @Override
    public void keyReleased(KeyCode key) {
        super.keyReleased(key);
    }

    private void gotoNextMove() {
        MoveNode move = game.getCurrentMove();
        if (!move.getChildren().isEmpty())
            game.setCurrentMove(move.getChildren().get(0));
    }

    private void gotoPreviousMove() {
        MoveNode move = game.getCurrentMove();
        if (move.getParent() != null)
            game.setCurrentMove(move.getParent());
    }
}
