package io.nibby.qipan.ui.tree;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameListener;
import io.nibby.qipan.game.MoveNode;
import io.nibby.qipan.ui.CanvasContainer;
import io.nibby.qipan.ui.board.Stone;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.BorderPane;

/**
 * The super-level parent of the game tree component.
 * It consists of a tree canvas and two scroll-bars.
 */
public class GameTreeUI extends BorderPane implements GameListener {

    private Game game;
    private CanvasContainer container;
    private GameTreeCanvas canvas;
    private ScrollBar hScroll; // Horizontal
    private ScrollBar vScroll; // Vertical

    // Scroll offsets
    private double xScroll = 0d;
    private double yScroll = 0d;


    public GameTreeUI(Game game) {
        this.game = game;
        this.game.addListener(this);
        this.setPrefWidth(300);

        canvas = new GameTreeCanvas(this);
        container = new CanvasContainer(canvas);
        setCenter(container);

        hScroll = new ScrollBar();
        hScroll.setOrientation(Orientation.HORIZONTAL);
        hScroll.setVisible(false);
        widthProperty().addListener(e -> {
            hScroll.setMaxWidth(getWidth() - 16);
        });
        setBottom(hScroll);

        vScroll = new ScrollBar();
        vScroll.setOrientation(Orientation.VERTICAL);
        vScroll.setVisible(false);
        setRight(vScroll);
    }

    /*
        Updates the tree data
     */
    private void update() {

    }

    /*
        Reconstructs the widget structure and recalculates all positioning.
     */
    private void reindexWidgetData() {

    }

    private void render() {
        canvas.render();
    }

    @Override
    public void movePlayed(Stone[] board, int x, int y, int color) {
        reindexWidgetData();
        update();
    }

    @Override
    public void currentMoveChanged(MoveNode currentMove) {
        update();
    }
}
