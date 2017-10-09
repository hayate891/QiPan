package io.nibby.qipan;

import io.nibby.qipan.board.BoardContainer;
import io.nibby.qipan.board.TestBoardController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class QiPan extends Application {

    public static final String NAME = "QiPan";
    public static final String VERSION = "v0.1";
    public static final String TITLE = NAME + " " + VERSION;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Temporary
        BorderPane pane = new BorderPane();
        BoardContainer container = new BoardContainer(19, 19, new TestBoardController());
        pane.setCenter(container);
        Scene scene = new Scene(pane, 800, 600);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
