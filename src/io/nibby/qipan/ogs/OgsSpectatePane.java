package io.nibby.qipan.ogs;

import javafx.scene.layout.BorderPane;

public class OgsSpectatePane extends BorderPane implements OgsContentPane {

    private OgsClientWindow client;

    public OgsSpectatePane(OgsClientWindow client) {
        this.client = client;
    }

    @Override
    public void updateContent() {

    }

}
