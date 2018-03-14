package io.nibby.qipan.ui.review;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;

public class ReviewMenubar extends MenuBar {

    private ReviewWindow window;

    public ReviewMenubar(ReviewWindow window) {
        this.window = window;

        // TODO temporary
        getMenus().add(new Menu("File"));
        getMenus().add(new Menu("Edit"));
        getMenus().add(new Menu("View"));
        getMenus().add(new Menu("Tools"));
        getMenus().add(new Menu("Window"));
        getMenus().add(new Menu("Help"));
    }

}
