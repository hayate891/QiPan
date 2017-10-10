package io.nibby.qipan.board;

import io.nibby.qipan.game.Game;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

/*
    A container to organize BoardCanvas + BoardInputCanvas.
 */
public class BoardContainer extends Pane {

    BoardMetrics metrics = new BoardMetrics();
    Game game;
    AbstractGameController boardController;
    final BoardCanvas boardView;
    final BoardInputCanvas boardInputView;

    public BoardContainer(Game game, AbstractGameController controller) {
        setGame(game);
        metrics.recalculate(this);

        this.boardController = controller;
        boardView = new BoardCanvas(this);
        boardInputView = new BoardInputCanvas(this);
        getChildren().addAll(boardView, boardInputView);
        this.boardController.onAdd(this);
        boardInputView.toFront();
        setCursor(Cursor.HAND);

        widthProperty().addListener(e -> {
            updateSize(getWidth(), getHeight());
        });
        heightProperty().addListener(e -> {
            updateSize(getWidth(), getHeight());
        });
    }

    private void updateSize(double width, double height) {
        super.setPrefSize(width, height);
        boardView.setWidth(width);
        boardView.setHeight(height);
        boardInputView.setWidth(width);
        boardInputView.setHeight(height);

        metrics.recalculate(this);
        render();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();

        boardView.setLayoutX(x);
        boardView.setLayoutY(y);
        boardView.setWidth(w);
        boardView.setHeight(h);
        boardInputView.setLayoutX(x);
        boardInputView.setLayoutY(y);
        boardInputView.setWidth(w);
        boardInputView.setHeight(h);
    }

    void render() {
        boardView.render();
        boardInputView.render();
    }

    public void setGame(Game game) {
        this.game = game;
        metrics.recalculate(this);

        if (boardView != null && boardInputView != null)
            render();
    }
}
