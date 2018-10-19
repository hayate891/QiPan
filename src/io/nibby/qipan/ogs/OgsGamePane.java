package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.sound.Sound;
import io.nibby.qipan.ui.CanvasContainer;
import io.nibby.qipan.ui.Renderable;
import io.nibby.qipan.ui.board.BoardBackgroundStyle;
import io.nibby.qipan.ui.board.BoardUI;
import io.nibby.qipan.ui.board.Stone;
import io.nibby.qipan.ui.board.StoneStyle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

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
        sidebar.setPrefWidth(220);
        {
            VBox playerInfos = new VBox();
            playerInfos.setSpacing(5);
            playerInfos.setFillWidth(true);

            PlayerInfoWidget playerBlack = new PlayerInfoWidget(Stone.BLACK, ogsGame);
            PlayerInfoWidget playerWhite = new PlayerInfoWidget(Stone.WHITE, ogsGame);

            CanvasContainer c1 = new CanvasContainer(playerBlack);
            c1.setPrefHeight(PlayerInfoWidget.WIDGET_HEIGHT);
            VBox.setMargin(c1, new Insets(17.5d, 0d, 0d, 0d));
            CanvasContainer c2 = new CanvasContainer(playerWhite);
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
        private GraphicsContext g;

        public PlayerInfoWidget(int color, OgsGameData gameData) {
            g = getGraphicsContext2D();
            this.playerColor = color;
            bgColor = playerColor == Stone.BLACK ? new Color(0d, 0d, 0d, 0.4d)
                                                 : new Color(1d, 1d, 1d, 0.45d);
            render();
        }

        @Override
        public void render() {
            g.setFill(bgColor);
            g.fillRect(15 + MARGIN, 15 + MARGIN, getWidth() - 2 * MARGIN, getHeight() - 2 * MARGIN);

            StoneStyle style = Settings.gui.getGameStoneStyle();
            style.render(g, 0, 0, 48, new Stone(playerColor, -1, -1));
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
