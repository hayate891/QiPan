package io.nibby.qipan;

import io.nibby.qipan.editor.SgfEditorWindow;
import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameRules;
import io.nibby.qipan.ogs.OgsClientWindow;
import io.nibby.qipan.ogs.OgsLoginWindow;
import io.nibby.qipan.settings.Settings;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class QiPan extends Application {

    public static final String NAME = "QiPan";
    public static final String VERSION = "v0.2";
    public static final String TITLE = NAME + " " + VERSION;
    public static final String CHARSET = "UTF-8";
    public static Font SYSTEM_FONT;

    @Override
    public void start(Stage primaryStage) {
        System.setProperty("file.encoding", "UTF-8");
        Game game = new Game(19, 19, GameRules.JAPANESE);
        SgfEditorWindow sgfEditorWindow = new SgfEditorWindow(game);
        sgfEditorWindow.show();

//        OgsLoginWindow ogs = new OgsLoginWindow();
//        ogs.show();

//        //TODO TEMPORARY FOR NOW
//        if (Settings.ogsAuth.tokenExists() && !Settings.ogsAuth.isTokenExpired()) {
//            ogs.close();
//
//            OgsClientWindow window = new OgsClientWindow();
//            window.show();
//        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
