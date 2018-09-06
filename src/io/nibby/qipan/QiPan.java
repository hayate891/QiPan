package io.nibby.qipan;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameRules;
import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.editor.SgfEditorWindow;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class QiPan extends Application {

    public static final String NAME = "QiPan";
    public static final String VERSION = "v0.1.2";
    public static final String TITLE = NAME + " " + VERSION;
    public static final String CHARSET = "UTF-8";
    public static Font SYSTEM_FONT;

    private LeelazHandler leelaz;

    @Override
    public void start(Stage primaryStage) {
        System.setProperty("file.encoding", "UTF-8");
        Game game = new Game(19, 19, GameRules.CHINESE);
        SgfEditorWindow sgfEditorWindow = new SgfEditorWindow(game);
        sgfEditorWindow.show();

        leelaz = new LeelazHandler(this);
        leelaz.start();
    }

    public LeelazHandler getLeelaHandler() {
        return leelaz;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
