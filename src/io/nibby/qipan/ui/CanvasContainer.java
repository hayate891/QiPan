package io.nibby.qipan.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class CanvasContainer extends Pane {

    private Canvas canvas;

    public CanvasContainer(Canvas canvas) {
        this.canvas = canvas;
        getChildren().addAll(canvas);
        canvas.toFront();

        widthProperty().addListener(e -> {
            updateSize(getWidth(), getHeight());
        });
        heightProperty().addListener(e -> {
            updateSize(getWidth(), getHeight());
        });
    }

    private void updateSize(double width, double height) {
        super.setPrefSize(width, height);
        canvas.setWidth(width);
        canvas.setHeight(height);
    }

    protected void layoutChildren() {
        super.layoutChildren();
        final double x = snappedLeftInset();
        final double y = snappedTopInset();
        final double w = snapSize(getWidth()) - x - snappedRightInset();
        final double h = snapSize(getHeight()) - y - snappedBottomInset();

        canvas.setLayoutX(x);
        canvas.setLayoutY(y);
        canvas.setWidth(w);
        canvas.setHeight(h);
    }

}
