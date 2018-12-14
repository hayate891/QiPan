package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.sound.Sound;
import io.nibby.qipan.ui.CanvasContainer;
import io.nibby.qipan.ui.Renderable;
import io.nibby.qipan.ui.board.BoardBackgroundStyle;
import io.nibby.qipan.ui.board.BoardUI;
import io.nibby.qipan.ui.board.Stone;
import io.nibby.qipan.ui.board.StoneStyle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/*
    This is the window that displays a single OGS game.
 */
public class OgsGamePane extends BorderPane {

    private OgsGameData ogsGame;
    private OgsGameController controller;
    private OgsService ogs;
    private BoardUI goban;
    private StackPane stackPane;
    private BorderPane contentRoot;
    private BorderPane sidebar;
    private BackgroundCanvas background;

    private PlayerInfoWidget pInfoBlack, pInfoWhite;

    public OgsGamePane() {
        stackPane = new StackPane();
        setCenter(stackPane);

        background = new BackgroundCanvas();
        CanvasContainer container = new CanvasContainer(background);
        contentRoot = new BorderPane();
        contentRoot.setCenter(goban);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(container);
        stackPane.getChildren().add(contentRoot);
        stackPane.widthProperty().addListener(e -> {
            contentRoot.setPrefWidth(stackPane.getWidth());
            container.updateSize(stackPane.getWidth(), stackPane.getHeight());
        });
        stackPane.heightProperty().addListener(e -> {
            contentRoot.setPrefHeight(stackPane.getHeight());
            container.updateSize(stackPane.getWidth(), stackPane.getHeight());
        });

        sidebar = new BorderPane();
        sidebar.getStyleClass().add("ogs-gamepane-sidebar");
        sidebar.setPrefWidth(260);
        {
            VBox playerInfos = new VBox();
            playerInfos.setSpacing(5);
            playerInfos.setFillWidth(true);

            pInfoBlack = new PlayerInfoWidget(Stone.BLACK);
            pInfoWhite = new PlayerInfoWidget(Stone.WHITE);

            CanvasContainer c1 = new CanvasContainer(pInfoBlack);
            c1.setPrefHeight(PlayerInfoWidget.WIDGET_HEIGHT);
            VBox.setMargin(c1, new Insets(17.5d, 0d, 0d, 0d));
            CanvasContainer c2 = new CanvasContainer(pInfoWhite);
            c2.setPrefHeight(PlayerInfoWidget.WIDGET_HEIGHT);
            playerInfos.getChildren().addAll(c1, c2);
            sidebar.setTop(playerInfos);

            playerInfos.widthProperty().addListener(l -> {
                int w = (int) playerInfos.getWidth();
                int h = PlayerInfoWidget.WIDGET_HEIGHT;
                c1.updateSize(w, h);
                c2.updateSize(w, h);
            });
            playerInfos.heightProperty().addListener(l -> {
                int w = (int) playerInfos.getWidth();
                int h = PlayerInfoWidget.WIDGET_HEIGHT;
                c1.updateSize(w, h);
                c2.updateSize(w, h);
            });
        }
        contentRoot.setRight(sidebar);
        contentRoot.widthProperty().addListener(l -> {
            sidebar.setPrefWidth(260d);
        });
    }

    /**
     * Invoked when the OGSService has established a successful connection to the desired game.
     *
     * @param connection
     */
    public void onConnection(OgsGameData connection) {
        ogsGame = connection;
        controller = new OgsGameController();
        goban = new BoardUI(ogsGame.getGame(), controller);
        contentRoot.setCenter(goban);
        pInfoBlack.update(ogsGame);
        pInfoWhite.update(ogsGame);
    }

    /**
     * Invoked when OGSService receives a move played event on the game.
     *
     * @param x x co-ordinate of the move played
     * @param y y co-ordinate of the move played
     * @param time Time in milliseconds for this move to be played since last move.
     */
    public void onMovePlayed(int x, int y, int time) {
        controller.placeMove(x, y, new Sound.ActionCallback() {
            @Override
            public void performAction() {

            }
        });
    }

    public OgsGameData getGameData() {
        return ogsGame;
    }

    static class PlayerInfoWidget extends Canvas implements Renderable {

        public static final int WIDGET_HEIGHT = 120;
        public static final int MARGIN = 5;

        private int playerColor;
        private Color bgColor;
        private Color headerColor;
        private Color titleColor;
        private GraphicsContext g;
        private OgsGameData gameData;
        private OgsPlayer player;

        public PlayerInfoWidget(int color) {
            g = getGraphicsContext2D();
            this.playerColor = color;
            bgColor = playerColor == Stone.BLACK ? new Color(0d, 0d, 0d, 0.4d)
                                                 : new Color(1d, 1d, 1d, 0.45d);
            titleColor = playerColor == Stone.BLACK ? Color.WHITE : Color.BLACK;
            headerColor = playerColor == Stone.BLACK ? Color.BLACK : Color.WHITE;
        }

        public void update(OgsGameData data) {
            this.gameData = data;
            this.player = playerColor == Stone.BLACK ? gameData.getPlayerBlack() : gameData.getPlayerWhite();
            this.player.getIcon().progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.intValue() == 1)
                        render();
                }
            });
            render();
        }

        @Override
        public void render() {
            g.clearRect(0, 0, getWidth(), getHeight());
            g.setFill(bgColor);
            g.fillRect(10 + MARGIN, 5 + MARGIN, getWidth() - 2 * MARGIN, getHeight() - 2 * MARGIN);

            if (player != null) {
                Text name = new Text(player.getUsername());
                name.applyCss();
                double nameWidth = name.getLayoutBounds().getWidth();
                g.setFill(bgColor);
                g.fillRect(10 + MARGIN, 5 + MARGIN, getWidth(), 30);
                g.setFill(titleColor);
                g.fillText(player.getUsername(), 60, 23 + MARGIN);
            }

            g.setFill(headerColor);
            g.fillOval(0, 0, 50, 50);
//            StoneStyle style = Settings.gui.getGameStoneStyle();
//            style.render(g, 0, 0, 48, new Stone(playerColor, -1, -1));

            if (player != null && player.getIcon() != null) {
                ImageView imgView = new ImageView(player.getIcon());
                imgView.setClip(new Circle(player.getIcon().getWidth() / 2, player.getIcon().getHeight() / 2,
                        player.getIcon().getWidth() / 2));
                SnapshotParameters params = new SnapshotParameters();
                params.setFill(Color.TRANSPARENT);
                WritableImage img = imgView.snapshot(params, null);
                imgView.setClip(null);
                imgView.setImage(img);
                g.drawImage(imgView.getImage(), 1, 1, 48, 48);
            }
        }
    }

    static class BackgroundCanvas extends Canvas implements Renderable {

        private GraphicsContext g;

        public BackgroundCanvas() {
            g = getGraphicsContext2D();
        }

        @Override
        public void render() {
            g.drawImage(BoardBackgroundStyle.TATAMI.getTexture(), 0, 0, getWidth(), getHeight());
        }
    }
}
