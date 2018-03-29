package io.nibby.qipan;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameRules;
import io.nibby.qipan.ui.review.ReviewWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class QiPan extends Application {

    public static final String NAME = "QiPan";
    public static final String VERSION = "v0.1.2";
    public static final String TITLE = NAME + " " + VERSION;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Temporary
        Game game = new Game(19, 19, GameRules.CHINESE);
        ReviewWindow reviewWindow = new ReviewWindow(game);
        reviewWindow.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
