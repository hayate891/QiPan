package io.nibby.qipan.ui.board;

import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameListener;
import io.nibby.qipan.game.MoveNode;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;

/*
    A container to organize BoardCanvas + BoardInputCanvas.
 */
public class BoardUI extends Pane implements GameListener {

    private BoardMetrics metrics = new BoardMetrics();
    private Game game;
    private AbstractGameController boardController;
    private final BoardCanvas boardView;
    private final BoardInputCanvas boardInputView;

    private BoardStyle boardStyle = BoardStyle.KAYA;
    private BoardBackgroundStyle boardBgStyle = BoardBackgroundStyle.TATAMI;
    private StoneStyle stoneStyle = StoneStyle.CERAMIC;

    public BoardUI(Game game, AbstractGameController controller) {
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
        this.game.addListener(this);
        metrics.recalculate(this);

        if (boardView != null && boardInputView != null)
            render();
    }

    public BoardStyle getBoardStyle() {
        return boardStyle;
    }

    public void setBoardStyle(BoardStyle boardStyle) {
        this.boardStyle = boardStyle;
    }

    public BoardBackgroundStyle getBoardBgStyle() {
        return boardBgStyle;
    }

    public void setBoardBgStyle(BoardBackgroundStyle boardBgStyle) {
        this.boardBgStyle = boardBgStyle;
    }

    public StoneStyle getStoneStyle() {
        return stoneStyle;
    }

    public void setStoneStyle(StoneStyle stoneStyle) {
        this.stoneStyle = stoneStyle;
    }

    public BoardMetrics getMetrics() {
        return metrics;
    }

    public Game getGame() {
        return game;
    }

    public AbstractGameController getBoardController() {
        return boardController;
    }

    public BoardCanvas getBoardView() {
        return boardView;
    }

    public BoardInputCanvas getBoardInputView() {
        return boardInputView;
    }

    @Override
    public void movePlayed(Stone[] board, int x, int y, int color) {
        render();
    }

    @Override
    public void currentMoveChanged(MoveNode currentMove) {
        render();
    }
}
