package io.nibby.qipan.ogs;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class OgsGameListPane extends BorderPane implements OgsContentPane {

    private OgsClientWindow client;
    private VBox gameListPane;

    public OgsGameListPane(OgsClientWindow client) {
        this.client = client;

        gameListPane = new VBox();
        setCenter(gameListPane);
    }

    @Override
    public void updateContent() {
        OgsService ogs = client.getOgsService();
        Rest.Response r = null;
        try {
            r = Rest.get("https://online-go.com/api/v1/me/games/?page=58", true);
            System.out.println(r.getRawString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
