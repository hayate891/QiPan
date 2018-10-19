package io.nibby.qipan.ui.board;

import io.nibby.qipan.game.Game;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/*
    This is the canvas component that renders the board position.
 */
public class BoardCanvas extends Canvas {

    private static final DropShadow TEXTURE_SHADOW = new DropShadow();
    private static final Color TEXTURE_SHADOW_COLOR = Color.color(0.25d, 0.25d, 0.25d, 0.25d);
    private static final int TEXTURE_SHADOW_MARGIN = 10;

    static {
        // TODO may be temporary, need to scale it with board size
        TEXTURE_SHADOW.setRadius(15);
        TEXTURE_SHADOW.setOffsetX(10);
        TEXTURE_SHADOW.setOffsetY(10);
        TEXTURE_SHADOW.setColor(Color.color(0d, 0d, 0d, 0.5d));
    }

    private BoardUI container;
    private BoardStyle boardStyle;
    private BoardBackgroundStyle backgroundStyle;
    private Color markerColor;
    private GraphicsContext g;

    public BoardCanvas(BoardUI container) {
        this.container = container;
        setFocusTraversable(true);
        g = getGraphicsContext2D();

        boardStyle = container.getBoardStyle();
        backgroundStyle = container.getBoardBgStyle();
        markerColor = boardStyle.getMarkerColor();
    }

    public void render() {
        BoardMetrics metrics = container.getMetrics();
        double gridSize = metrics.gridSize;
        int boardWidth = metrics.boardWidth;
        int boardHeight = metrics.boardHeight;
        double offsetX = metrics.offsetX;
        double offsetY = metrics.offsetY;
        double gridOffsetX = metrics.gridOffsetX;
        double gridOffsetY = metrics.gridOffsetY;
        double gap = metrics.gap;

        g.clearRect(0, 0, getWidth(), getHeight());
        {
            // Draw the backdrop
            g.drawImage(backgroundStyle.getTexture(), 0, 0, getWidth(), getHeight());
//            g.setFill(new Color(0.941d, 0.941d, 0.941d, 1.0d));
//            g.fillRect(0, 0, getWidth(), getHeight());
            // Draw the board
            double width = gridSize * boardWidth;
            double height = gridSize * boardHeight;
            double x = getWidth() / 2 - width / 2;
            double y = getHeight() / 2 - height / 2;

            g.setEffect(TEXTURE_SHADOW);
            g.setFill(TEXTURE_SHADOW_COLOR);
            g.fillRect(x - TEXTURE_SHADOW_MARGIN, y - TEXTURE_SHADOW_MARGIN,
                    width + TEXTURE_SHADOW_MARGIN * 2, height + TEXTURE_SHADOW_MARGIN * 2);
            g.setEffect(null);
            g.drawImage(boardStyle.getTexture(), x, y, width, height);

            g.setFill(markerColor);
            g.setStroke(markerColor);
        }

        // Board lines
        for (int x = 0; x < boardWidth; x++) {
            g.strokeLine(metrics.getGridX(x),offsetY + gridOffsetY, metrics.getGridX(x),
                    metrics.getGridY(boardHeight - 1));
        }

        for (int y = 0; y < boardHeight; y++) {
            g.strokeLine(offsetX + gridOffsetX, metrics.getGridY(y),
                    metrics.getGridX(boardWidth - 1), metrics.getGridY(y));
        }

        // Board star points
        double dotSize = gridSize / 6;
        int centerDot = (boardWidth % 2 == 1) ? (boardWidth - 1) / 2 : -1;

        if (boardWidth == boardHeight && (boardWidth == 9 || boardWidth == 13 || boardWidth == 19)) {
            int corner = boardWidth == 9 ? 2 : 3;
            double grid = gridSize - gap;

            if (centerDot != -1)
                g.fillOval(metrics.getGridX(centerDot) - dotSize / 2,
                        metrics.getGridY(centerDot) - dotSize / 2, dotSize, dotSize);

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

        // Board stones
        Game game = container.getGame();
        Stone[] stones = game.getStones();
        for (Stone stone : stones) {
            if (stone == null)
                continue;
            if (stone.shouldWobble())
                continue;

            stone.render(g, container.getMetrics(), container.getStoneStyle());
            if (stone.getX() == game.getCurrentMove().getMoveX()
                    && stone.getY() == game.getCurrentMove().getMoveY()) {
                // TODO temporary move highlighter

                double size = metrics.stoneSize / 2;

                double x = metrics.getBoardStoneX(stone.getX()) + stone.wobbleX + metrics.stoneSize / 2 - size / 2;
                double y = metrics.getBoardStoneY(stone.getY()) + stone.wobbleY + metrics.stoneSize / 2 - size / 2;
                if (container.getStoneStyle().fuzzyPlacement()) {
                    double o = metrics.stoneSize / 16;
                    x += stone.fuzzyX + o;
                    y += stone.fuzzyY + o;
                }
                Color color = container.getStoneStyle().annotationColor(stone);
                g.setStroke(color);
                g.setLineWidth(2);
                g.strokeOval(x, y, size, size);
                g.setLineWidth(1);
            }
        }
    }
}
