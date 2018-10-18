package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OgsActiveGamePane extends BorderPane implements OgsContentPane {

    private OgsClientWindow client;
    private OgsService ogs;

    private Map<Integer, Tab> tabData = new HashMap<>();
    private TabPane gameTabs;

    public OgsActiveGamePane(OgsClientWindow client) {
        this.client = client;
        this.ogs = client.getOgsService();

        gameTabs = new TabPane();
        setCenter(gameTabs);
    }

    @Override
    public void updateContent() {

    }

    public void addGame(OgsDashboardPane.GameInfo info, OgsGamePane pane) {
        ResourceBundle bundle = Settings.language.getLocale().getBundle("OgsClient");
        String title = bundle.getString("client.game.game") + " " + info.gameId;
        Tab tab = new Tab(title, pane);
        tab.setOnClosed(evt -> {
            ogs.disconnectFromGame(info.gameId);
        });
        gameTabs.getTabs().add(tab);
    }

    public void showGameTab(int gameId) {
        Tab tab = tabData.get(gameId);
        if (tab != null) {
            gameTabs.getSelectionModel().select(tab);
        }
    }
}
