package io.nibby.qipan.ogs;

import com.sun.istack.internal.NotNull;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class OgsClientWindow extends Stage {

    private OgsPlayer player;
    private OgsService ogs;

    private Rectangle dialogBackground;
    private Pane dialogObject;
    private Scene scene;
    private BorderPane root;

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

    public OgsClientWindow() {
        ogs = new OgsService();
        ResourceBundle bundle = Settings.language.getLocale().getBundle("OgsClient");
        {
            sidebar = new BorderPane();
            sidebar.getStyleClass().add("client-sidebar");

            VBox topVbox = new VBox();
            topVbox.setAlignment(Pos.TOP_LEFT);
            topVbox.getStyleClass().add("client-sidebar");
            topVbox.setFillWidth(true);
            topVbox.setSpacing(5);
            {
                // Placeholder logo
                textLogoTemp = new Text();
                textLogoTemp.setStyle("-fx-font-weight: bold; -fx-font-size: 26px; -fx-fill: rgb(200, 200, 200);");
                textLogoTemp.setText("OGS");
                textLogoTemp.applyCss();
                VBox.setMargin(textLogoTemp, new Insets(20, 20, 15, 20));
                topVbox.getChildren().add(textLogoTemp);

                // TODO user topVbox info module

                toggleGroup = new ToggleGroup();

                // Menu buttons
                buttonPlay = new MenuButton(bundle.getString("client.sidebar.play"));
//                buttonPlay.getStyleClass().add("play-button");
                {
//                    MenuItem playStandard = new MenuItem(bundle.getString("client.topVbox.play"));
//                    playStandard.setOnAction(evt -> {
//                        showDialogPane(new CreateGameStandardPane(), false);
//                    });

//                    MenuItem playDemo = new MenuItem(Localization.getBundle().getString("core.topVbox.button.play.demo"));
//                    playDemo.setOnAction(evt -> {
//                    screen.setContentView(new CreateGameDemoPane());
//                        showDialogPane(new CreateGameDemoPane(screen), false);
//                    });

//                    buttonPlay.getItems().addAll(playStandard, playDemo);
                }
                addSidebarButton(topVbox, buttonPlay, "play");

                buttonDashboard = new ToggleButton(bundle.getString("client.sidebar.dashboard"));
                buttonDashboard.setSelected(true);
                buttonDashboard.setOnAction(evt -> {
//                    setContentView(getDashboardView());
                });
                addSidebarButton(topVbox, buttonDashboard, "home");

                buttonSpectate = new ToggleButton(bundle.getString("client.sidebar.spectate"));
                buttonSpectate.setOnAction(evt -> {
//                    screen.setContentView(new SpectateGameView());
                });
                addSidebarButton(topVbox, buttonSpectate, "spectate");
            }
            sidebar.setCenter(topVbox);

            VBox botVbox = new VBox();
            botVbox.setAlignment(Pos.TOP_LEFT);
            botVbox.getStyleClass().add("client-sidebar");
            botVbox.setFillWidth(true);
            botVbox.setSpacing(5);
            {
//                buttonProfile = new ToggleButton(Localization.getBundle().getString("core.topVbox.button.profile"));
//                buttonProfile.setOnAction(evt -> {
//                    screen.setContentView(new UserProfileView());
//                });
//                addSidebarButton(botVbox, buttonProfile, "profile");

                buttonSettings = new ToggleButton(bundle.getString("client.sidebar.settings"));
                buttonSettings.setOnAction(evt -> {
//                    screen.setContentView(new SettingsView());
                });
                addSidebarButton(botVbox, buttonSettings, "settings");

                buttonLogout = new Button(bundle.getString("client.sidebar.logout"));
                addSidebarButton(botVbox, buttonLogout, "logout");

                // Gap
                VBox gap = new VBox();
                gap.setMinHeight(25);
                botVbox.getChildren().add(gap);
            }
            sidebar.setBottom(botVbox);
        }

        root = new BorderPane();
        root.setLeft(sidebar);
        scene = new Scene(root, 800, 600);
        UIStylesheets.applyTo(scene);
        setTitle("QiPan: OGS");
        setScene(scene);
        setResizable(true);

        setOnShowing(this::startService);
        setOnCloseRequest(this::stopService);

    }

    private void stopService(WindowEvent windowEvent) {
        ogs.disconnect();
        System.exit(0);
    }

    private void startService(WindowEvent windowEvent) {
        ogs.connect();
    }

    private void addSidebarButton(VBox vbox, ButtonBase b, String icon) {
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
        b.setMinWidth(180);
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

                        // screen.setContentView(null);
                    }
                }
            });
        }
    }

    public void showDialogPane(Pane pane, boolean modal) {
        dialogBackground = new Rectangle(scene.getWidth(), scene.getHeight());
        dialogBackground.widthProperty().bind(root.widthProperty());
        dialogBackground.heightProperty().bind(root.heightProperty());
        dialogBackground.setFill(Color.BLACK);

        this.dialogObject = pane;

        if (!modal) {
            dialogBackground.setOnMouseClicked(evt -> {
                dismissDialogPane(true);
            });
        }

        StackPane.setAlignment(dialogBackground, Pos.CENTER);
        StackPane.setAlignment(pane, Pos.CENTER);

        root.getChildren().add(dialogBackground);
        root.getChildren().add(pane);

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
                root.getChildren().remove(dialogBackground);
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
                root.getChildren().remove(dialogObject);
            });
            fadePane.play();
        } else {
            root.getChildren().remove(dialogObject);
            root.getChildren().remove(dialogBackground);
        }
    }

    public OgsService getOgsService() {
        return ogs;
    }

    //
//    private void openSocket(WindowEvent windowEvent) {
//        try {

//            Socket socket = IO.socket(new URL("https://online-go.com").toURI().toString(), options);
//
//            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
//                @Override
//                public void call(Object... objects) {
//                    System.out.println("Error");
//                    if (objects[0] instanceof Exception) {
//                        ((Exception) objects[0]).printStackTrace();
//                    }
//                }
//            });
//
//            socket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
//                @Override
//                public void call(Object... objects) {
//                    for (Object o : objects) {
//                        System.out.println(o);
//                    }
//                }
//            });
//            socket.on(Socket.EVENT_CONNECT, e -> {
//
//
//            });
//            socket.connect();
//
//            int game =14849657;
//            Rest.Response rr = Rest.get("https://online-go.com/api/v1/games/" + game, true);
//            JSONObject obj = rr.getJson();
//            String auth = obj.getString("auth");
//            String chatAuth = obj.getString("game_chat_auth");
//
//            JSONObject jj = new JSONObject();
//            jj.put("player_id", player.getId());
//            jj.put("username", player.getUsername());
//            jj.put("auth", auth);
//            socket.emit("authenticate", jj);
//
//
//            JSONObject json = new JSONObject();
//            json.put("game_id", game);
//            json.put("player_id", player.getId());
////            json.put("auth", auth);
//            json.put("chat", false);
//            socket.on("game/" + game + "/gamedata", objs -> {
//                for (Object o : objs) {
//                    System.out.println(o.toString());
//                }
//            });
//            socket.on("game/" + game + "/move", objs -> {
//                for (Object o : objs) {
//                    System.out.println(o.toString());
//                }
//            });
//
//            socket.emit("game/connect", json);
//
//            JSONObject j = new JSONObject();
//            j.put("player_id", player.getId());
//            j.put("auth", auth);
//            j.put("game_id", game);
//            j.put("move", "ab");
//            socket.emit("game/move", j);
//
//            JSONObject j2 = new JSONObject();
//            j2.put("auth", chatAuth);
//            j2.put("game_id", game);
//            j2.put("player_id", player.getId());
//            j2.put("body", "test_message");
//            j2.put("type", "discussion");
//            j2.put("move_number", 3);
//            j2.put("username", player.getUsername());
//            j2.put("is_player", true);
//            j2.put("ranking", player.getRank());
//            j2.put("ui_class", "blah?");
//
//            socket.emit("game/chat", j2);

//            JSONObject j = new JSONObject();
//            j.put("move", "ab");
//            Rest.Response r = Rest.postBody("https://online-go.com/api/v1/games/"+ game + "/resign/", true, "");
//            System.out.println("Got: " + r.getRawString());
//            System.out.println("R: " + r.getHttpResponse());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }

    public void setSessionPlayer(@NotNull  OgsPlayer player) {
        this.player = player;
    }

}
