package io.nibby.qipan.editor;

import io.nibby.qipan.settings.Settings;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

import java.util.ResourceBundle;

public class SgfEditorMenuBar extends MenuBar {

    private SgfEditorWindow window;

    public SgfEditorMenuBar(SgfEditorWindow window) {
        this.window = window;
        getStyleClass().add("menubar");

        // TODO temporary
        ResourceBundle bundle = Settings.language.getLocale().getBundle("SgfEditor");
        Menu file = new Menu(bundle.getString("editor.menu.file"));
        {
            MenuItem fileNew = new MenuItem(bundle.getString("editor.menu.file.new"));
            fileNew.setAccelerator(KeyCombination.valueOf("Ctrl+N"));
            file.getItems().add(fileNew);

            MenuItem fileOpen = new MenuItem(bundle.getString("editor.menu.file.open"));
            fileOpen.setAccelerator(KeyCombination.valueOf("Ctrl+O"));
            file.getItems().add(fileOpen);

            MenuItem fileSave = new MenuItem(bundle.getString("editor.menu.file.save"));
            fileSave.setAccelerator(KeyCombination.valueOf("Ctrl+S"));
            file.getItems().add(fileSave);

            MenuItem fileSaveAs = new MenuItem(bundle.getString("editor.menu.file.save-as"));
            fileSaveAs.setAccelerator(KeyCombination.valueOf("Ctrl+Shift+S"));
            file.getItems().add(fileSaveAs);
        }
        getMenus().add(file);
        getMenus().add(new Menu(bundle.getString("editor.menu.edit")));
        getMenus().add(new Menu(bundle.getString("editor.menu.view")));
        getMenus().add(new Menu(bundle.getString("editor.menu.tools")));
        getMenus().add(new Menu(bundle.getString("editor.menu.window")));
        getMenus().add(new Menu(bundle.getString("editor.menu.help")));
    }

}
