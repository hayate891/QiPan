package io.nibby.qipan.board;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/*
    This is the canvas component that renders the board position.
 */
public class BoardCanvas extends Canvas {

    private static final DropShadow TEXTURE_SHADOW = new DropShadow();
    private static final Color TEXTURE_SHADOW_COLOR = Color.color(0.25d, 0.25d, 0.25d, 0.25d);
    private static final int TEXTURE_SHADOW_MARGIN = 10;
    private static final Color LINE_COLOR = Color.color(158d / 255d, 103d / 255d, 35d / 255d);

    static {
        // TODO may be temporary
        TEXTURE_SHADOW.setRadius(15);
        TEXTURE_SHADOW.setOffsetX(10);
        TEXTURE_SHADOW.setOffsetY(10);
        TEXTURE_SHADOW.setColor(Color.color(0d, 0d, 0d, 0.5d));
    }

    private BoardContainer container;
    private Image texture;
    private GraphicsContext g;

    public BoardCanvas(BoardContainer container) {
        this.container = container;
        setFocusTraversable(true);
        g = getGraphicsContext2D();

        //TODO temporary
        texture = new Image(BoardBackground.KAYA.getResource());
    }

    public void render() {
        double gridSize = container.gridSize;
        int boardWidth = container.boardWidth;
        int boardHeight = container.boardHeight;
        double offsetX = container.offsetX;
        double offsetY = container.offsetY;
        double gridOffsetX = container.gridOffsetX;
        double gridOffsetY = container.gridOffsetY;
        double gap = container.gap;

        g.clearRect(0, 0, getWidth(), getHeight());
        if (texture != null) {
            double width = gridSize * boardWidth;
            double height = gridSize * boardHeight;
            double x = getWidth() / 2 - width / 2;
            double y = getHeight() / 2 - height / 2;

            g.setEffect(TEXTURE_SHADOW);
            g.setFill(TEXTURE_SHADOW_COLOR);
            g.fillRect(x - TEXTURE_SHADOW_MARGIN, y - TEXTURE_SHADOW_MARGIN,
                    width + TEXTURE_SHADOW_MARGIN * 2, height + TEXTURE_SHADOW_MARGIN * 2);
            g.setEffect(null);
            g.drawImage(texture, x, y, width, height);
        }

        g.setFill(LINE_COLOR);
        g.setStroke(LINE_COLOR);

        // Board lines
        for (int x = 0; x < boardWidth; x++) {
            g.strokeLine(getDrawX(x),offsetY + gridOffsetY, getDrawX(x), getDrawY(boardHeight - 1));
        }

        for (int y = 0; y < boardHeight; y++) {
            g.strokeLine(offsetX + gridOffsetX, getDrawY(y), getDrawX(boardWidth - 1), getDrawY(y));
        }

        // Board dots
        double dotSize = gridSize / 6;
        int centerDot = (boardWidth % 2 == 1) ? (boardWidth - 1) / 2 : -1;

        if (boardWidth == boardHeight && (boardWidth == 9 || boardWidth == 13 || boardWidth == 19)) {
            int corner = boardWidth == 9 ? 2 : 3;
            double grid = gridSize - gap;

            if (centerDot != -1)
                g.fillOval(getDrawX(centerDot) - dotSize / 2,
                        getDrawY(centerDot) - dotSize / 2, dotSize, dotSize);

            g.fillOval(gridOffsetX + offsetX + corner * grid - dotSize / 2,
                    gridOffsetY + offsetY + corner * grid - dotSize / 2, dotSize, dotSize);
            g.fillOval(gridOffsetX + offsetX + (boardWidth - corner - 1) * grid - dotSize / 2,
                    gridOffsetY + offsetY + corner * grid - dotSize / 2, dotSize, dotSize);
            g.fillOval(gridOffsetX + offsetX + corner * grid - dotSize / 2,
                    gridOffsetY + offsetY + (boardHeight - corner - 1) * grid - dotSize / 2, dotSize, dotSize);
            g.fillOval(gridOffsetX + offsetX + (boardWidth - corner - 1) * grid - dotSize / 2,
                    gridOffsetY + offsetY + (boardHeight - corner - 1) * grid - dotSize / 2, dotSize, dotSize);

            if (boardWidth == 19) {
                g.fillOval(gridOffsetX + offsetX + centerDot * grid - dotSize / 2,
                        gridOffsetY + offsetY + corner * grid - dotSize / 2, dotSize, dotSize);
                g.fillOval(gridOffsetX + offsetX + centerDot * grid - dotSize / 2,
                        gridOffsetY + offsetY + (boardHeight - corner - 1) * grid - dotSize / 2, dotSize, dotSize);
                g.fillOval(gridOffsetX + offsetX + corner * grid - dotSize / 2,
                        gridOffsetY + offsetY + centerDot * grid - dotSize / 2, dotSize, dotSize);
                g.fillOval(gridOffsetX + offsetX + (boardWidth - corner - 1) * grid - dotSize / 2,
                        gridOffsetY + offsetY + centerDot * grid - dotSize / 2, dotSize, dotSize);
            }
        }
    }

    private double getDrawX(int x) {
        return container.gridOffsetX + container.offsetX + x * (container.gridSize - container.gap);
    }

    private double getDrawY(int y) {
        return container.gridOffsetY + container.offsetY + y * (container.gridSize - container.gap);
    }
}
