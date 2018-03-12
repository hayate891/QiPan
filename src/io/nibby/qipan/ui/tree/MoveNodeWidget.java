package io.nibby.qipan.ui.tree;

import javafx.scene.canvas.GraphicsContext;

/**
 * A small canvas representation of the move node in the game tree.
 * Most of the data stored here is related to its rendering.
 *
 * The widget has two main draw modes: DISPLAY_FULL and DISPLAY_SMALL.
 * DISPLAY_FULL draws the move node with as much info as possible.
 * DISPLAY_SMALL draws the move node with an icon and move co-ordinate only.
 */
public class MoveNodeWidget {

    // Display constants
    public static final int DISPLAY_FULL = 0;
    public static final int DISPLAY_SMALL = 1;

    // Size constants (pixels)
    public static final int DISPLAY_FULL_WIDTH = 48;
    public static final int DISPLAY_FULL_HEIGHT = 24;

    public static final int DISPLAY_SMALL_WIDTH = 24;
    public static final int DISPLAY_SMALL_HEIGHT = 24;

    /*
        Display co-ordinates on canvas.
     */
    private int x;
    private int y;
    private int width;
    private int height;
    private int displayMode;

    public MoveNodeWidget(int x, int y, int displayMode) {
        this.x = x;
        this.y = y;
        setDisplayMode(displayMode);
    }

    public void setDisplayMode(int displayMode) {
        this.displayMode = displayMode;
        switch (displayMode) {
            case DISPLAY_FULL:
                this.width = DISPLAY_FULL_WIDTH;
                this.height = DISPLAY_FULL_HEIGHT;
                break;
            case DISPLAY_SMALL:
                this.width = DISPLAY_SMALL_WIDTH;
                this.height = DISPLAY_SMALL_HEIGHT;
                break;
        }
    }

    public void render(GraphicsContext g) {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDisplayMode() {
        return displayMode;
    }
}
