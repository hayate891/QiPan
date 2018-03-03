package io.nibby.qipan.board;

import io.nibby.qipan.game.Game;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/*
    This is the canvas component that handles input/draws stone placement hints
    as well as stone wobbles.

    It is kept separate from the position canvas to avoid redrawing the entire board
    on each input repaint.
 */
public class BoardInputCanvas extends Canvas {

    public static final int HINT_NULL = 0;
    public static final int HINT_STONE_BLACK = 1;
    public static final int HINT_STONE_WHITE = 2;
    public static final int HINT_ANNOTATION_CIRCLE = 3;
    public static final int HINT_ANNOTATION_TRIANGLE = 4;
    public static final int HINT_ANNOTATION_SQUARE = 5;
    public static final int HINT_ANNOTATION_CROSS = 6;
    public static final int HINT_ANNOTATION_NUMBERS = 7;
    public static final int HINT_ANNOTATION_LETTERS = 8;
    public static final int HINT_ANNOTATION_DIM_STONE = 9;

    private List<Stone> wobbleStones = new ArrayList<>();
    private BoardContainer container;
    private AbstractGameController controller;
    private GraphicsContext g;

    private int mouseX = -1;
    private int mouseY = -1;
    private int lastMouseX = 1;
    private int lastMouseY = -1;
    private int cursorHint = HINT_NULL;

    public BoardInputCanvas(BoardContainer container) {
        this.container = container;
        this.controller = container.boardController;
        setFocusTraversable(true);
        g = getGraphicsContext2D();

        addEventHandler(MouseEvent.MOUSE_CLICKED, this::mouseClicked);
        addEventHandler(MouseEvent.MOUSE_MOVED, this::mouseMoved);
        addEventHandler(MouseEvent.MOUSE_PRESSED, this::mousePressed);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, this::mouseDragged);
        addEventHandler(ScrollEvent.SCROLL, this::scroll);
        addEventHandler(KeyEvent.KEY_PRESSED, this::keyPressed);
        addEventHandler(KeyEvent.KEY_RELEASED, this::keyReleased);
    }

    void render() {
        g.clearRect(0, 0, getWidth(), getHeight());
        // Wobbling stones are drawn here with a 20ms refresh timer
        if (wobbleStones.size() > 0) {
            for (int i = 0; i < wobbleStones.size();) {
                Stone stone = wobbleStones.get(i);
                stone.wobble();

                if (!stone.shouldWobble()) {
                    wobbleStones.remove(i);
                    container.boardView.render();
                } else {
                    stone.render(g, container.metrics);
                    i++;
                }
            }

            new Timeline(new KeyFrame(Duration.millis(20), e -> {
                render();
            })).play();
        } else
            container.boardView.render();

        // TODO implement later
    }

    private void updateMousePosition(MouseEvent evt) {
        BoardMetrics metrics = container.metrics;
        double offsetX = metrics.offsetX;
        double offsetY = metrics.offsetY;
        double gridOffsetX = metrics.gridOffsetX;
        double gridOffsetY = metrics.gridOffsetY;
        double gridSize = metrics.gridSize - metrics.gap;

        double mx = evt.getX() - offsetX - gridOffsetX + gridSize / 3;
        double my = evt.getY() - offsetY - gridOffsetY + gridSize / 3;

        mouseX = (int) (mx / gridSize);
        mouseY = (int) (my / gridSize);

        if (mouseX < 0 || mouseY < 0 || mouseX > metrics.boardWidth - 1
                || mouseY > metrics.boardHeight - 1) {
            mouseX = -1;
            mouseY = -1;
            return;
        }

        if (lastMouseX != mouseX || lastMouseY != mouseY) {
            lastMouseX = mouseX;
            lastMouseY = mouseY;
        }
    }

    private void mouseClicked(MouseEvent evt) {
        requestFocus();
        updateMousePosition(evt);
        controller.mouseClicked(mouseX, mouseY, lastMouseX, lastMouseY, evt.getButton());
        render();
    }

    private void mouseMoved(MouseEvent evt) {
        requestFocus();
        updateMousePosition(evt);
        controller.mouseMoved(mouseX, mouseY, lastMouseX, lastMouseY);
        render();
    }

    private void mousePressed(MouseEvent evt) {
        requestFocus();
        updateMousePosition(evt);
        controller.mousePressed(mouseX, mouseY, lastMouseX, lastMouseY, evt.getButton());
        render();
    }

    private void mouseDragged(MouseEvent evt) {
        requestFocus();
        updateMousePosition(evt);
        controller.mouseDragged(mouseX, mouseY, lastMouseX, lastMouseY);
        render();
    }

    private void scroll(ScrollEvent evt) {
        requestFocus();
        controller.mouseScrolled(evt.getDeltaY());
        render();
    }

    private void keyPressed(KeyEvent evt) {
        requestFocus();
        controller.keyPressed(evt.getCode());
        render();
    }

    private void keyReleased(KeyEvent evt) {
        requestFocus();
        controller.keyReleased(evt.getCode());
        render();
    }

    protected void addWobbleStone(Stone stone) {
        wobbleStones.add(stone);
    }

    protected boolean isWobbleStone(Stone stone) {
        return wobbleStones.contains(stone);
    }
}
