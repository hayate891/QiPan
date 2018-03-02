package io.nibby.qipan.board;

import javafx.scene.image.Image;

public enum BoardBackgroundStyle {

    TATAMI("Tatami", "0.jpg")
    ;

    private String name;
    private Image texture;

    BoardBackgroundStyle(String name, String textureResource) {
        this.name = name;
        this.texture = new Image(BoardBackgroundStyle.class.getResourceAsStream("/background/" + textureResource));
    }

    public String getName() {
        return name;
    }

    public Image getTexture() {
        return texture;
    }
}
