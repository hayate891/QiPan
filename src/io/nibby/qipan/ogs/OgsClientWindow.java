package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.UIStylesheets;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OgsClientWindow extends Stage {

    private OgsService ogs;

    private Rectangle dialogBackground;
    private Pane dialogObject;
    private Scene scene;
    private BorderPane rootPane;
    private BorderPane contentPane;

    private BorderPane sidebar;
    private Text textLogoTemp;
    private ToggleButton buttonDashboard;
    private MenuButton buttonPlay;
    private ToggleButton buttonSpectate;
    private ToggleButton buttonChat;
    private ToggleButton buttonWebsite;
    private ToggleButton buttonSettings;
    private Button buttonLogout;
    private ToggleGroup toggleGroup;

    private OgsDashboardPane dashboardPane;
    private OgsSpectatePane spectatePane;
    private OgsSettingsPane settingsPane;

    public OgsClientWindow() {
        ogs = new OgsService(this);
        ResourceBundle bundle = Settings.language.getLocale().getBundle("OgsClient");
        {
            sidebar = new BorderPane();
            sidebar.getStyleClass().add("client-sidebar");

            HBox topVbox = new HBox();
            topVbox.setAlignment(Pos.CENTER_LEFT);
            topVbox.getStyleClass().add("client-sidebar");
            topVbox.setFillHeight(true);
            topVbox.setSpacing(3);
            topVbox.setPadding(new Insets(0, 0, 0, 15));
            {
                // Placeholder logo
                textLogoTemp = new Text();
                textLogoTemp.getStyleClass().add("ogs-logo-text");
                textLogoTemp.setText("OGS");
                textLogoTemp.applyCss();
                HBox.setMargin(textLogoTemp, new Insets(0, 10, 0, 0));
                topVbox.getChildren().add(textLogoTemp);

                // TODO user topVbox info module
                toggleGroup = new ToggleGroup();

                // Menu buttons
                buttonPlay = new MenuButton(bundle.getString("client.sidebar.play"));
                {
                    MenuItem play19 = new MenuItem(bundle.getString("client.sidebar.play.19"));
                    buttonPlay.getItems().addAll(play19);

                    MenuItem play13 = new MenuItem(bundle.getString("client.sidebar.play.13"));
                    buttonPlay.getItems().addAll(play13);

                    MenuItem play9 = new MenuItem(bundle.getString("client.sidebar.play.9"));
                    buttonPlay.getItems().addAll(play9);

                    MenuItem playCustom = new MenuItem(bundle.getString("client.sidebar.play.custom"));
                    buttonPlay.getItems().addAll(playCustom);
                }
                addMenuButton(topVbox, buttonPlay, "play");

                buttonDashboard = new ToggleButton(bundle.getString("client.sidebar.dashboard"));
                buttonDashboard.setSelected(true);
                buttonDashboard.setOnAction(evt -> {
                    setContentPane(dashboardPane);
                });
                addMenuButton(topVbox, buttonDashboard, "home");

                buttonSpectate = new ToggleButton(bundle.getString("client.sidebar.spectate"));
                buttonSpectate.setOnAction(evt -> {
                    setContentPane(spectatePane);
                });
                addMenuButton(topVbox, buttonSpectate, "spectate");
            }
            sidebar.setLeft(topVbox);

            HBox botVbox = new HBox();
            botVbox.setAlignment(Pos.CENTER_RIGHT);
            botVbox.getStyleClass().add("client-sidebar");
            botVbox.setFillHeight(true);
            botVbox.setSpacing(3);
            botVbox.setPadding(new Insets(0, 0, 0, 0));
            {
                buttonSettings = new ToggleButton(bundle.getString("client.sidebar.settings"));
                buttonSettings.setOnAction(evt -> {
                    setContentPane(settingsPane);
                });
                addMenuButton(botVbox, buttonSettings, "settings");

                buttonLogout = new Button(bundle.getString("client.sidebar.logout"));
                addMenuButton(botVbox, buttonLogout, "logout");

                // Gap
                VBox gap = new VBox();
                gap.setMinHeight(25);
                botVbox.getChildren().add(gap);
            }
            sidebar.setRight(botVbox);
            sidebar.setPadding(new Insets(0, 0, 0, 0));
        }

        rootPane = new BorderPane();
        rootPane.setPadding(new Insets(0, 0, 0, 0));
        rootPane.setTop(sidebar);

        contentPane = new BorderPane();
        {
            dashboardPane = new OgsDashboardPane(this);
            setContentPane(dashboardPane);

            spectatePane = new OgsSpectatePane(this);
            settingsPane = new OgsSettingsPane(this);

        }
        rootPane.setCenter(contentPane);

        scene = new Scene(rootPane, 800, 600);
        UIStylesheets.applyTo(scene);
        setTitle("QiPan: OGS");
        setScene(scene);
        setResizable(true);
        setMinWidth(800);
        setMinHeight(600);

        setOnShowing(this::startService);
        setOnCloseRequest(this::stopService);
    }

    private void setContentPane(Pane pane) {
        if (contentPane.getCenter() != null) {
            contentPane.getChildren().remove(contentPane.getCenter());
        }

        contentPane.setCenter(pane);
        if (pane instanceof OgsContentPane) {
            ((OgsContentPane) pane).updateContent();
            if (!contentPane.getStyleClass().contains("ogs-content-pane")) {
                contentPane.getStyleClass().add("ogs-content-pane");
            }
        }
    }

    private void stopService(WindowEvent windowEvent) {
        ogs.shutdown();
        System.exit(0);
    }

    private void startService(WindowEvent windowEvent) {
        new Thread(() -> {
            try {
                ogs.initialize();

//                // TODO testing
//                Platform.runLater(() -> {
//                    OgsGamePane window = ogs.openGame(14882508);
//                    setContentPane(window);
//                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addMenuButton(HBox vbox, ButtonBase b, String icon) {
        String iconPath = "/ogs/" + icon + ".png";
        URL iconUrl = OgsClientWindow.class.getResource(iconPath);
        if (iconUrl != null) {
            ImageView iconView = new ImageView(iconUrl.toString());
            iconView.setFitWidth(16);
            iconView.setFitHeight(16);
            b.setGraphic(iconView);
        }
        b.setGraphicTextGap(10);
        b.getStyleClass().add("sidebar-item");
        if (!b.getText().isEmpty())
            b.setMinWidth(80);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setTextAlignment(TextAlignment.LEFT);
        VBox.setMargin(b, new Insets(0, 20, 0, 20));
        vbox.getChildren().add(b);

        if (b instanceof ToggleButton) {
            ToggleButton toggle = (ToggleButton) b;
            toggleGroup.getToggles().add(toggle);

            toggle.selectedProperty().addListener(e -> {
                boolean selected = toggle.isSelected();
                if (!selected) {
                    boolean hasSelection = false;
                    for (Toggle t : toggleGroup.getToggles()) {
                        if (t.isSelected()) {
                            hasSelection = true;
                            break;
                        }
                    }

                    if (!hasSelection) {
                        toggle.setSelected(true);
                    }
                }
            });
        }
    }

    public void showDialogPane(Pane pane, boolean modal) {
        dialogBackground = new Rectangle(scene.getWidth(), scene.getHeight());
        dialogBackground.widthProperty().bind(rootPane.widthProperty());
        dialogBackground.heightProperty().bind(rootPane.heightProperty());
        dialogBackground.setFill(Color.BLACK);

        this.dialogObject = pane;

        if (!modal) {
            dialogBackground.setOnMouseClicked(evt -> {
                dismissDialogPane(true);
            });
        }

        StackPane.setAlignment(dialogBackground, Pos.CENTER);
        StackPane.setAlignment(pane, Pos.CENTER);

        rootPane.getChildren().add(dialogBackground);
        rootPane.getChildren().add(pane);

        FadeTransition fadeRect = new FadeTransition(Duration.millis(500), dialogBackground);
        fadeRect.setFromValue(0d);
        fadeRect.setToValue(0.5d);
        fadeRect.setCycleCount(1);
        fadeRect.setAutoReverse(false);
        fadeRect.setInterpolator(Interpolator.EASE_IN);
        fadeRect.play();

        double x = 0;
        double fromY = getScene().getHeight() / 3;
        double toY = 0;

        TranslateTransition paneTransition = new TranslateTransition(Duration.millis(500), pane);
        paneTransition.setFromX(x);
        paneTransition.setToX(x);
        paneTransition.setFromY(fromY);
        paneTransition.setToY(toY);
        paneTransition.setCycleCount(1);
        paneTransition.setAutoReverse(false);
        paneTransition.play();

        FadeTransition fadePane = new FadeTransition(Duration.millis(500), pane);
        fadePane.setFromValue(0d);
        fadePane.setToValue(1d);
        fadePane.setCycleCount(1);
        fadePane.setAutoReverse(false);
        fadePane.setInterpolator(Interpolator.EASE_IN);
        fadePane.play();
    }

    public void dismissDialogPane(boolean animated) {
        if (animated) {
            FadeTransition fadeRect = new FadeTransition(Duration.millis(500), dialogBackground);
            fadeRect.setFromValue(0.5d);
            fadeRect.setToValue(0d);
            fadeRect.setCycleCount(1);
            fadeRect.setAutoReverse(false);
            fadeRect.setInterpolator(Interpolator.EASE_IN);
            fadeRect.setOnFinished(evt -> {
                rootPane.getChildren().remove(dialogBackground);
            });
            fadeRect.play();

            double x = 0;
            double fromY = 0;
            double toY = getScene().getHeight() / 3;

            TranslateTransition paneTransition = new TranslateTransition(Duration.millis(500), dialogObject);
            paneTransition.setFromX(x);
            paneTransition.setToX(x);
            paneTransition.setFromY(fromY);
            paneTransition.setToY(toY);
            paneTransition.setCycleCount(1);
            paneTransition.setAutoReverse(false);
            paneTransition.play();

            FadeTransition fadePane = new FadeTransition(Duration.millis(500), dialogObject);
            fadePane.setFromValue(1d);
            fadePane.setToValue(0d);
            fadePane.setCycleCount(1);
            fadePane.setAutoReverse(false);
            fadePane.setInterpolator(Interpolator.EASE_OUT);
            fadePane.setOnFinished(evt -> {
                rootPane.getChildren().remove(dialogObject);
            });
            fadePane.play();
        } else {
            rootPane.getChildren().remove(dialogObject);
            rootPane.getChildren().remove(dialogBackground);
        }
    }

    public OgsService getOgsService() {
        return ogs;
    }

    public OgsDashboardPane getDashboardPane() {
        return dashboardPane;
    }

    public OgsSpectatePane getSpectatePane() {
        return spectatePane;
    }

    public OgsSettingsPane getSettingsPane() {
        return settingsPane;
    }
}
