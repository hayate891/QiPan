package io.nibby.qipan.board;

import javafx.scene.paint.Color;

public enum BoardStyle {

    PLAIN("Plain", "plain.jpg", Color.color(0d, 0d, 0d, 1d)),
    KAYA("Light Kaya", "kaya.jpg", Color.color(158d / 255d, 103d / 255d, 35d / 255d));

    private String name;
    private String textureResource;
    // Color for grid lines + star points
    private Color markerColor;

    BoardStyle(String name, String textureResource, Color markerColor) {
        this.name = name;
        this.textureResource = textureResource;
        this.markerColor = markerColor;
    }

    public String getName() {
        return name;
    }

    public String getTextureResource() {
        return "/board/" + textureResource;
    }

    public Color getMarkerColor() {
        return markerColor;
    }
}
