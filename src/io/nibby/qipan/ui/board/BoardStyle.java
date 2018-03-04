package io.nibby.qipan.ui.board;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public enum BoardStyle {

    PLAIN("Plain", "plain.jpg", Color.color(0d, 0d, 0d, 1d)),
    KAYA("Light Kaya", "kaya.jpg", Color.color(158d / 255d, 103d / 255d, 35d / 255d));

    private String name;
    private Image texture;
    // Color for grid lines + star points
    private Color markerColor;

    BoardStyle(String name, String textureResource, Color markerColor) {
        this.name = name;
        this.texture = new Image(BoardStyle.class.getResourceAsStream("/board/" + textureResource));
        this.markerColor = markerColor;
    }

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }

    public Color getMarkerColor() {
        return markerColor;
    }
}
