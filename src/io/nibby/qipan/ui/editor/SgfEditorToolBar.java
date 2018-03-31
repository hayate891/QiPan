package io.nibby.qipan.ui.editor;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class SgfEditorToolBar extends ToolBar {

    private SgfEditorWindow window;

    public SgfEditorToolBar(SgfEditorWindow window) {
        this.window = window;

        Pane leftPadder = new Pane();
        HBox.setHgrow(leftPadder, Priority.ALWAYS);

        getItems().add(new MenuButton("Edit", null, new MenuItem("View Only"), new MenuItem("Theatre")));
    }

}
