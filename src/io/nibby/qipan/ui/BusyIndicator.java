package io.nibby.qipan.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BusyIndicator extends Canvas {

    static private final String[] FRAME_LOOP = {
            "0120" +
                    "1012" +
                    "0120",

            "0120" +
                    "1202" +
                    "0120",
    };

    static private final String[] FRAME_DONE = {
            "0120" +
                    "1112" +
                    "0120",

            "0120" +
                    "1222" +
                    "0120",
    };

    static private final int STONE_SIZE = 12;

    private Color colorBackground;
    private Color colorWhite;
    private Color colorBlack;

    private int currentFrame = 0;

    boolean fillWhite;
    boolean fillBlack;

    private boolean done = false;
    private GraphicsContext graphics;
    private Timeline timeline;

    public BusyIndicator() {
        graphics = getGraphicsContext2D();

        colorBackground = Color.WHITE;
        fillWhite = false;
        colorWhite = Color.BLACK;

        fillBlack = true;
        colorBlack = Color.BLACK;

        setWidth(STONE_SIZE * 4 + 4);
        setHeight(STONE_SIZE * 3 + 4);
        setVisible(false);
    }

    public void start() {
        setVisible(true);
        done = false;
        draw();

        if (timeline != null)
            timeline.stop();

        timeline = new Timeline(
                new KeyFrame(Duration.millis(750),
                        evt -> {
                            currentFrame++;
                            currentFrame %= 2;
                            draw();
                        }
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void stop() {
        done = true;
        timeline.stop();
        draw();
    }

    private void draw() {
        graphics.setFill(colorBackground);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        String frame = done ? FRAME_DONE[currentFrame == 0 ? 1 : 0] : FRAME_LOOP[currentFrame];
        for (int i = 0; i < frame.length(); i++) {
            char ch = frame.charAt(i);

            if (ch == '0')
                continue;

            int x = i % 4;
            int y = i / 4;
            graphics.setFill(ch == '1' ? colorBlack : colorWhite);
            graphics.setStroke(ch == '1' ? colorBlack : colorWhite);

            if (ch == '1' && fillBlack || ch == '2' && fillWhite)
                graphics.fillOval(2 + x * STONE_SIZE, 2 + y * STONE_SIZE, STONE_SIZE, STONE_SIZE);
            else
                graphics.strokeOval(2 + x * STONE_SIZE, 2 + y * STONE_SIZE, STONE_SIZE, STONE_SIZE);
        }
    }

}