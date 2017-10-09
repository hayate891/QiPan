package io.nibby.qipan.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

/*
    This is the canvas component that handles input/draws stone placement hint.
    It is kept separate from the position canvas to avoid redrawing the entire board
    on each input repaint.
 */
public class BoardInputCanvas extends Canvas {

    private BoardContainer container;
    private GraphicsContext g;

    public BoardInputCanvas(BoardContainer container) {
        this.container = container;
        setFocusTraversable(true);
        g = getGraphicsContext2D();
    }

    public void render() {
        // TODO implement later
    }
}
