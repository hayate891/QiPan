package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OgsDashboardPane extends BorderPane implements OgsContentPane {

    private OgsClientWindow client;
    private ActiveGamePane activeGamePane;
    private List<GameInfo> activeGames = new ArrayList<>();
    private Text activeGameHeader;
    private ResourceBundle bundle;

    public OgsDashboardPane(OgsClientWindow client) {
        this.client = client;
        bundle = Settings.language.getLocale().getBundle("OgsClient");

        BorderPane content = new BorderPane();
        activeGamePane = new ActiveGamePane();
        content.setCenter(activeGamePane);
        setCenter(content);
    }

    @Override
    public void updateContent() {
        activeGamePane.indexItems();
    }

    public void addActiveGame(GameInfo info) {
        activeGames.add(info);
        activeGamePane.indexItems();
    }

    public class ActiveGamePane extends BorderPane {

        private VBox items;
        private ScrollPane scrollpane;

        public ActiveGamePane() {
            setPadding(new Insets(20, 20, 20, 20));


            activeGameHeader = new Text(bundle.getString("client.dashboard.active-games"));
            activeGameHeader.getStyleClass().add("ogs-client-header");
            BorderPane.setMargin(activeGameHeader, new Insets(0, 0, 0, 20));
            setTop(activeGameHeader);

            items = new VBox();
            items.setPadding(new Insets(5, 5, 5, 5));
            items.setAlignment(Pos.TOP_CENTER);
            items.setFillWidth(true);
            items.setSpacing(5);
            scrollpane = new ScrollPane(items);
            scrollpane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollpane.setFitToHeight(true);
            scrollpane.setFitToWidth(true);
            setCenter(scrollpane);
        }

        private void indexItems() {
            items.getChildren().removeAll(items.getChildren());

            List<GameInfo> theirTurn = new ArrayList<>();
            for (GameInfo info : activeGames) {
                if (info.playerIdToMove == client.getOgsService().getSessionPlayer().getId()) {
                    ActiveGameItem item = new ActiveGameItem(info, this);
                    items.getChildren().add(item);
                } else
                    theirTurn.add(info);
            }
            for (GameInfo info : theirTurn) {
                ActiveGameItem item = new ActiveGameItem(info, this);
                items.getChildren().add(item);
            }
            activeGameHeader.setText(bundle.getString("client.dashboard.active-games") + " (" + items.getChildren().size() + ")");
        }
    }

    public class ActiveGameItem extends BorderPane {

        private boolean myTurn;
        private GameInfo info;
        private ActiveGamePane parent;

        public ActiveGameItem(GameInfo info, ActiveGamePane parent) {
            this.parent = parent;
            myTurn = info.playerIdToMove == client.getOgsService().getSessionPlayer().getId();
            this.info = info;
            setPrefHeight(50);

            Color color = myTurn ? Color.OLIVE : Color.WHITE;
            Background bg = new Background(new BackgroundFill(color, new CornerRadii(3), new Insets(0, 0, 0, 0)));
            setBackground(bg);
        }
    }

    public static class GameInfo {
        // Uses OgsGame gamephase constants
        public int gamePhase;
        public long paused;
        public boolean privateGame;
        public int playerIdToMove;
        public OgsPlayer playerBlack;
        public OgsPlayer playerWhite;
        public int boardWidth;
        public int boardHeight;
        public int gameId;
        public int timePerMove;
        public int moveNumber;
        public String gameName;
    }
}
