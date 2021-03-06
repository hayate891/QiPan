package io.nibby.qipan.ui;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class UIStylesheets {

    public static final List<String> styleSheets = new ArrayList<>();

    public static void add(String stylesheet) {
        styleSheets.add(stylesheet);
    }

    public static void applyTo(Scene scene) {
        scene.getStylesheets().addAll(styleSheets);
    }
}
