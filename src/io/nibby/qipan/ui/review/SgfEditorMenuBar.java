package io.nibby.qipan.ui.review;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class SgfEditorMenuBar extends MenuBar {

    private SgfEditorWindow window;

    public SgfEditorMenuBar(SgfEditorWindow window) {
        this.window = window;
        getStyleClass().add("menubar");

        // TODO temporary
        getMenus().add(new Menu("File"));
        getMenus().add(new Menu("Edit"));
        getMenus().add(new Menu("View"));
        getMenus().add(new Menu("Tools"));
        getMenus().add(new Menu("Window"));
        getMenus().add(new Menu("Help"));
    }

}
