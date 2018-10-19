package io.nibby.qipan.ogs;

import io.nibby.qipan.game.GameClock;
import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.board.Stone;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.*;

public class OgsDashboardPane extends BorderPane implements OgsContentPane {

    private OgsClientWindow client;
    private ActiveGamePane activeGamePane;
    private Map<Integer, GameInfo> activeGames = new HashMap<>();
    private Text activeGameHeader;
    private ResourceBundle bundle;

    private static final Color COLOR_GAME_PLAYERTURN = new Color(232d / 255d, 247d/255d, 1d, 1.0d);
    private static final Color COLOR_GAME_PLAYERTURN_HOVER = new Color(242d / 255d, 250d/255d, 1d, 1.0d);
    private static final Color COLOR_GAME_THEIRTURN = Color.WHITE;
    private static final Color COLOR_GAME_THEIRTURN_HOVER = new Color(0.97d, 0.97d, 0.97d, 1.0d);

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
        activeGames.put(info.gameId, info);
        activeGamePane.addItem(info);
    }

    public void updateActiveGame(GameInfo info) {
        if (activeGames.get(info.gameId) == null)
            addActiveGame(info);
        else {
            activeGamePane.updateItem(info);
        }
    }

    public class ActiveGamePane extends BorderPane {

        private VBox items;
        private Map<Integer, ActiveGameItem> itemMap = new HashMap<>();
        private ScrollPane scrollpane;

        public ActiveGamePane() {
            setPadding(new Insets(20, 20, 20, 20));

            activeGameHeader = new Text(bundle.getString("client.dashboard.active-games"));
            activeGameHeader.getStyleClass().add("ogs-client-header");
//            BorderPane.setMargin(activeGameHeader, new Insets(0, 0, 0, 0));
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

        private void updateItem(GameInfo info) {
            ActiveGameItem item = itemMap.get(info.gameId);
            if (item != null)
                item.update(info);
            else
                System.err.println("Cannot update active game: " + info.gameId);
        }

        private void addItem(GameInfo info) {
            ActiveGameItem item = new ActiveGameItem(info, this);
            items.getChildren().add(item);
            itemMap.put(info.gameId, item);
        }

        private void indexItems() {
            items.getChildren().removeAll(items.getChildren());

            List<GameInfo> theirTurn = new ArrayList<>();
            for (Integer key : activeGames.keySet()) {
                GameInfo info = activeGames.get(key);
                if (info.gamePhase != OgsGameData.GAME_PHASE_PLAYING)
                    continue;

                if (info.playerIdToMove == client.getOgsService().getSessionPlayer().getId()) {
                    addItem(info);
                } else
                    theirTurn.add(info);
            }
            for (GameInfo info : theirTurn) {
                addItem(info);
            }
            activeGameHeader.setText(bundle.getString("client.dashboard.active-games") + " (" + items.getChildren().size() + ")");
        }
    }

    public class ActiveGameItem extends BorderPane {

        private boolean myTurn;
        private GameInfo info;
        private ActiveGamePane parent;

        private Text lbGameName;
        private BadgePane moveNumBadge;
        private BadgePane boardSizeBadge;
        private Text playerBlack;
        private Text playerWhite;

        public ActiveGameItem(GameInfo info, ActiveGamePane parent) {
            this.parent = parent;
            myTurn = info.playerIdToMove == client.getOgsService().getSessionPlayer().getId();
            this.info = info;
            Color color = myTurn ? COLOR_GAME_PLAYERTURN : COLOR_GAME_THEIRTURN;
            Background bg = new Background(new BackgroundFill(color, new CornerRadii(5), new Insets(0, 0, 0, 0)));
            setBackground(bg);
            setPrefHeight(75);

            String moveNumText = bundle.getString("client.game.move-num") + " " + info.moveNumber;
            String boardSizeText = info.boardWidth + "x" + info.boardHeight;

            HBox gameMetaPane = new HBox();
            gameMetaPane.setFillHeight(false);
            gameMetaPane.setSpacing(5);
            gameMetaPane.setAlignment(Pos.CENTER_LEFT);
            moveNumBadge = new BadgePane(moveNumText, new Color(0.8d, 0.8d, 0.8d, 1.0d), Color.WHITE);
            gameMetaPane.getChildren().add(moveNumBadge);

            boardSizeBadge = new BadgePane(boardSizeText, new Color(1.0d, 198d/255d, 39d/255d, 1.0d), new Color(1.0d, 249d/255d, 219d/255d, 1.0d));
            gameMetaPane.getChildren().add(boardSizeBadge);

            lbGameName = new Text(info.gameName);
            gameMetaPane.getChildren().add(lbGameName);

            setTop(gameMetaPane);

            HBox playerMetaPane = new HBox();
            playerMetaPane.setPadding(new Insets(5, 0, 0, 0));
            setBottom(playerMetaPane);
            playerMetaPane.setAlignment(Pos.CENTER_LEFT);
            setPadding(new Insets(5));
            playerMetaPane.setSpacing(5);

            StoneIcon blackStone = new StoneIcon(Stone.BLACK);
            playerMetaPane.getChildren().add(blackStone);

            // TODO rank probably not accurate
            playerBlack = new Text(info.playerBlack.getUsername() + " [" + OgsPlayer.getRankText(info.playerBlack) + "]");
            playerMetaPane.getChildren().add(playerBlack);
            HBox.setMargin(playerBlack, new Insets(0, 10, 0 , 0));

            StoneIcon whiteStone = new StoneIcon(Stone.WHITE);
            playerMetaPane.getChildren().add(whiteStone);

            playerWhite = new Text(info.playerWhite.getUsername() + "[" + OgsPlayer.getRankText(info.playerWhite) + "]");
            playerMetaPane.getChildren().add(playerWhite);

            this.setOnMouseEntered(this::mouseHover);
            this.setOnMouseExited(this::mouseExit);
            this.setOnMouseClicked(this::mouseClick);
        }

        private void update(GameInfo info) {
            this.info = info;
            setMoveNumber(info.moveNumber);
            myTurn = info.playerIdToMove == client.getOgsService().getSessionPlayer().getId();
            Color color = myTurn ? COLOR_GAME_PLAYERTURN : COLOR_GAME_THEIRTURN;
            Background bg = new Background(new BackgroundFill(color, new CornerRadii(5), new Insets(0, 0, 0, 0)));
            setBackground(bg);
        }

        private void setMoveNumber(int move) {
            String moveNumText = bundle.getString("client.game.move-num") + " " + info.moveNumber;
            moveNumBadge.label.setText(moveNumText);
        }

        private void mouseClick(MouseEvent mouseEvent) {
            // TODO Bring up a sidebar and show the game preview before entering game

            // TODO adjust this temporary code
            client.joinGame(info.gameId);
        }

        private void mouseExit(MouseEvent mouseEvent) {
            if (myTurn)
                setBackground(COLOR_GAME_PLAYERTURN);
            else
                setBackground(COLOR_GAME_THEIRTURN);
        }

        private void mouseHover(MouseEvent mouseEvent) {
            if (myTurn)
                setBackground(COLOR_GAME_PLAYERTURN_HOVER);
            else
                setBackground(COLOR_GAME_THEIRTURN_HOVER);
        }

        public void setBackground(Color color) {
            BackgroundFill fill = new BackgroundFill(color, new CornerRadii(5), new Insets(0));
            Background bg = new Background(fill);
            this.setBackground(bg);
        }

        private class BadgePane extends HBox {

            private Text label;

            private BadgePane(String text, Color border, Color bg) {
                setBorder(new Border(new BorderStroke(border, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1))));
                BackgroundFill fill = new BackgroundFill(bg, new CornerRadii(3), new Insets(0, 0, 0, 0));
                setBackground(new Background(fill));

                label = new Text(text);
                getChildren().add(label);
                setAlignment(Pos.CENTER);
                setPadding(new Insets(3));
                setPrefHeight(20);
            }
        }

        private class StoneIcon extends Canvas {

            private GraphicsContext g;
            private int color;

            public StoneIcon(int color) {
                g = getGraphicsContext2D();
                this.color = color;

                setWidth(16);
                setHeight(16);
                this.render();
            }

            public void render() {
                double x = 1;
                double y = 1;
                double w = getWidth() - 2;
                double h = getHeight() - 2;
                g.setFill(color == Stone.BLACK ? Color.BLACK : Color.WHITE);
                g.fillOval(x, y, w, h);
                g.setStroke(color == Stone.BLACK ? Color.WHITE : Color.BLACK);
                g.strokeOval(x, y, w, h);
            }
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
        public int timePerMove; //TODO potentially missing param
        public int moveNumber;
        public String gameName;
        public GameClock clockSetting;
        public GameClock[] playerClocks;
    }
}
