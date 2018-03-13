package io.nibby.qipan;

import io.nibby.qipan.game.GameRules;
import io.nibby.qipan.ui.board.BoardUI;
import io.nibby.qipan.ui.board.DefaultBoardController;
import io.nibby.qipan.game.Game;
import io.nibby.qipan.ui.tree.GameTreeUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class QiPan extends Application {

    public static final String NAME = "QiPan";
    public static final String VERSION = "v0.1.1";
    public static final String TITLE = NAME + " " + VERSION;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Temporary
        Game game = new Game(19, 19, GameRules.CHINESE);
        BorderPane pane = new BorderPane();
        BoardUI container = new BoardUI(game, new DefaultBoardController());
        pane.setCenter(container);

        GameTreeUI tree = new GameTreeUI(game);
        BorderPane sidepane = new BorderPane();
        sidepane.setCenter(tree);
        pane.setBottom(sidepane);

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
