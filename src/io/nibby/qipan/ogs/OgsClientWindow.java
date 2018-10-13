package io.nibby.qipan.ogs;

import io.nibby.qipan.ui.UIStylesheets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class OgsClientWindow extends Stage {

    public OgsClientWindow() {

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        UIStylesheets.applyTo(scene);
        setTitle("QiPan: OGS");
        setScene(scene);
        setResizable(true);
    }

}
