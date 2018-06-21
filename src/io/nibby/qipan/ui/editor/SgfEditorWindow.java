package io.nibby.qipan.ui.editor;

import io.nibby.qipan.QiPan;
import io.nibby.qipan.game.Game;
import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.UIStylesheets;
import io.nibby.qipan.ui.board.BoardUI;
import io.nibby.qipan.ui.board.SgfEditorController;
import io.nibby.qipan.ui.tree.GameTreeUI;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ResourceBundle;

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
        ResourceBundle bundle = Settings.language.getLocale().getBundle("SgfEditor");
        BorderPane content = new BorderPane();

        BorderPane center = new BorderPane();
        mainMenubar = new SgfEditorMenuBar(this);
        center.setTop(mainMenubar);

        boardViewSplit = new SplitPane();
        boardViewSplit.setOrientation(Orientation.HORIZONTAL);
        boardUi = new BoardUI(game, new SgfEditorController());
        boardUi.setMinWidth(600);
        center.setCenter(boardUi);
        boardViewSplit.getItems().add(center);
        content.setCenter(boardViewSplit);

        treeViewSplit = new SplitPane();
        treeViewSplit.setOrientation(Orientation.VERTICAL);

        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("main-tab-pane");
        { // Tab 1 -- game tree
            treeUi = new GameTreeUI(game);
            Tab treeTab = new Tab(bundle.getString("editor.tabpane.gametree"), treeUi);
            tabPane.getTabs().add(treeTab);
        }

        moveComments = new TextArea();
        moveComments.getStyleClass().add("move-comments");
        moveComments.setWrapText(true);
        moveComments.setPromptText(bundle.getString("editor.comments.prompt"));

        BorderPane commentPane = new BorderPane();
        FlowPane commentHeader = new FlowPane();
        commentHeader.getStyleClass().add("move-comments-header");
        Label headerLabel = new Label(bundle.getString("editor.comments.header"));
        headerLabel.getStyleClass().add("move-comments-header-label");
        commentHeader.getChildren().add(headerLabel);
        commentPane.setTop(commentHeader);
        commentPane.setCenter(moveComments);

        treeViewSplit.getItems().addAll(tabPane, commentPane);
        treeViewSplit.setDividerPosition(0, 0.6d);
        boardViewSplit.getItems().add(treeViewSplit);
        boardViewSplit.setDividerPosition(0, 0.6d);

        Scene scene = new Scene(content, 800, 600);
        UIStylesheets.applyTo(scene);
        //TODO temporary
        setTitle(QiPan.TITLE);
        setScene(scene);
        setAlwaysOnTop(true);
        setMinWidth(850);
        setMinHeight(700);
    }

}
