package io.nibby.qipan.ui.review;

import io.nibby.qipan.QiPan;
import io.nibby.qipan.game.Game;
import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.board.BoardUI;
import io.nibby.qipan.ui.board.DefaultBoardController;
import io.nibby.qipan.ui.tree.GameTreeUI;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SgfEditorWindow extends Stage {

    private SgfEditorMenuBar mainMenubar;
    private SgfEditorToolBar mainToolbar;
    private SplitPane boardViewSplit;
    private BoardUI boardUi;
    private GameTreeUI treeUi;
    private SplitPane treeViewSplit;
    private TextArea moveComments;

    private Game game;

    public SgfEditorWindow(Game game) {
        this.game = game;
        setupUI();
    }

    private void setupUI() {
        VBox topPane = new VBox();

        BorderPane content = new BorderPane();
        mainMenubar = new SgfEditorMenuBar(this);
        topPane.getChildren().add(mainMenubar);

        mainToolbar = new SgfEditorToolBar(this);
        topPane.getChildren().add(mainToolbar);
        content.setTop(topPane);

        boardViewSplit = new SplitPane();
        boardViewSplit.setOrientation(Orientation.VERTICAL);
        boardUi = new BoardUI(game, new DefaultBoardController());
        boardViewSplit.getItems().add(boardUi);
        content.setCenter(boardViewSplit);

        treeViewSplit = new SplitPane();
        treeViewSplit.setOrientation(Orientation.HORIZONTAL);

        treeUi = new GameTreeUI(game);
        moveComments = new TextArea();

        treeViewSplit.getItems().addAll(treeUi, moveComments);
        treeViewSplit.setDividerPosition(0, 0.6d);
        boardViewSplit.getItems().add(treeViewSplit);
        boardViewSplit.setDividerPosition(0, 0.8d);

        Scene scene = new Scene(content, 800, 700);
        scene.getStylesheets().add(Settings.gui.getUiStyle().getStylesheet());
        //TODO temporary
        setTitle(QiPan.TITLE);
        setScene(scene);
        setAlwaysOnTop(true);
        setMinWidth(800);
        setMinHeight(700);
    }

}
