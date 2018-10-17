package io.nibby.qipan.ogs;

import io.nibby.qipan.Utility;
import io.nibby.qipan.editor.SgfEditorWindow;
import io.nibby.qipan.game.Game;
import io.nibby.qipan.game.GameRules;
import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.BusyIndicator;
import io.nibby.qipan.ui.UIStylesheets;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class OgsLoginWindow extends Stage {

    private boolean loginSuccess = false;
    private ResourceBundle bundle;
    private TextField fieldUsername;
    private PasswordField fieldPassword;
    private Button buttonLogin;
    private BusyIndicator indicator;

    private Label lbError;
    private Hyperlink lbForgotPassword;
    private Hyperlink lbRegister;

    public OgsLoginWindow() {
        bundle = Settings.language.getLocale().getBundle("OgsClient");

        Pane form = createForm();
        BorderPane logoPane = new BorderPane();
        logoPane.setCenter(new ImageView(new Image(OgsLoginWindow.class.getResourceAsStream("/ogs/logo.png"))));

        BorderPane root = new BorderPane();
        root.getStyleClass().add("ogs-login");
        root.setCenter(form);
        root.setTop(logoPane);
        root.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(root, 350, 500);
        setScene(scene);
        setResizable(false);
        setTitle("OGS: Online-Go.com");
        UIStylesheets.applyTo(scene);

        setOnShowing(e -> {
            if (Settings.ogsAuth.tokenExists()) {
                fieldUsername.setText(Settings.ogsAuth.getLastUsername());
                fieldPassword.requestFocus();
            }
        });

        setOnCloseRequest(e -> {
            if (!loginSuccess) {
                SgfEditorWindow window = new SgfEditorWindow(new Game(19, 19, GameRules.JAPANESE));
                window.show();
            }
        });
    }

    private Pane createForm() {
        VBox pane = new VBox();
        pane.setPadding(new Insets(0, 40, 0, 40));
        pane.setAlignment(Pos.CENTER);
        Label lbUsername = new Label(bundle.getString("login.username"));
        VBox.setMargin(lbUsername, new Insets(15, 5, 5, 5));
        lbUsername.getStyleClass().add("bold");
        pane.getChildren().add(lbUsername);

        fieldUsername = new TextField();
        lbUsername.setLabelFor(fieldUsername);
        fieldUsername.setPrefWidth(200);
        fieldUsername.setAlignment(Pos.CENTER);
        VBox.setMargin(fieldUsername, new Insets(5, 5, 5, 5));
        pane.getChildren().add(fieldUsername);

        Label lbPassword = new Label(bundle.getString("login.password"));
        lbPassword.getStyleClass().add("bold");
        VBox.setMargin(lbPassword, new Insets(15, 5, 5, 5));
        pane.getChildren().add(lbPassword);

        fieldPassword = new PasswordField();
        fieldPassword.setPrefWidth(200);
        fieldPassword.setAlignment(Pos.CENTER);
        VBox.setMargin(fieldPassword, new Insets(5, 5, 5, 5));
        pane.getChildren().add(fieldPassword);

        lbForgotPassword = new Hyperlink(bundle.getString("login.password-forgot"));
        lbForgotPassword.setOnAction(e -> {
            try {
                Utility.openWebpage(new URL("https://online-go.com/sign-in"));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        });
        pane.getChildren().add(lbForgotPassword);

        lbError = new Label(bundle.getString("login.error.credentials"));
        lbError.setVisible(false);
        lbError.setManaged(false);
        lbError.getStyleClass().add("error-label");
        VBox.setMargin(lbError, new Insets(5, 5, 5, 5));
        pane.getChildren().add(lbError);

        indicator = new BusyIndicator();
        indicator.start();
        indicator.setVisible(false);
        indicator.setManaged(false);
        VBox.setMargin(indicator, new Insets(25, 5, 5, 5));
        pane.getChildren().add(indicator);

        buttonLogin = new Button(bundle.getString("login.submit"));
        buttonLogin.setOnAction(this::submitLogin);
        VBox.setMargin(buttonLogin, new Insets(25, 5, 5, 5));
        pane.getChildren().add(buttonLogin);

        lbRegister = new Hyperlink(bundle.getString("login.register"));
        lbRegister.setOnAction(e -> {
            try {
                Utility.openWebpage(new URL("https://online-go.com/register"));
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
        });
        pane.getChildren().add(lbRegister);

        fieldUsername.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                buttonLogin.fire();
            }
        });

        fieldPassword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                buttonLogin.fire();
            }
        });

        return pane;
    }
    private void submitLogin(ActionEvent actionEvent) {
        //TODO work in progress

        String username = fieldUsername.getText();
        String password = fieldPassword.getText();

        buttonLogin.setDisable(true);
        buttonLogin.setVisible(false);
        buttonLogin.setManaged(false);
        indicator.setVisible(true);
        indicator.setManaged(true);
        indicator.start();
        lbError.setVisible(false);

        new Thread(() -> {
            // Rest request to OGS

            try {
                Rest.Response r = Rest.postParams("https://online-go.com/oauth2/token/", false,
                        "client_id", Settings.ogsAuth.getClientId(),
                        "client_secret", Settings.ogsAuth.getClientSecret(),
                        "grant_type", "password",
                        "username", username,
                        "password", password);

                if (r.getHttpResponse().getStatusLine().getStatusCode() != 200) {
                    // TODO an error has occurred
                    lbError.setVisible(true);
                    lbError.setManaged(true);
                    System.out.println("Error: " + r.getHttpResponse());
                    System.out.println("Error Raw: " + r.getRawString());
                    return;
                }

                loginSuccess = true;
                // Successful login
                JSONObject j = r.getJson();
                String authToken = j.getString("access_token");
                String reauthToken = j.getString("refresh_token");
                String scope = j.getString("scope");
                int expiry = j.getInt("expires_in");
                String tokenType = j.getString("token_type");

                Settings.ogsAuth.setAuthToken(authToken);
                Settings.ogsAuth.setReAuthToken(reauthToken);
                Settings.ogsAuth.setScope(scope);
                Settings.ogsAuth.setTokenExpiry(expiry * 1000);
                Settings.ogsAuth.setTokenAcquireTime(System.currentTimeMillis());
                Settings.ogsAuth.setTokenType(tokenType);
                Settings.ogsAuth.setLastUsername(username);
                Settings.ogsAuth.save();

                Platform.runLater(() -> {
                    buttonLogin.setDisable(false);
                    buttonLogin.setManaged(true);
                    buttonLogin.setVisible(true);
                    indicator.stop();
                    indicator.setVisible(false);
                    indicator.setManaged(false);

                    OgsLoginWindow.this.close();

                    OgsClientWindow window = new OgsClientWindow();
                    window.show();
                });

            } catch (IOException e) {
                //TODO error handling
                e.printStackTrace();
            }
        }).start();
    }

}
