package io.nibby.qipan.ogs;

import io.nibby.qipan.settings.Settings;
import io.nibby.qipan.ui.UIStylesheets;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OgsLoginWindow extends Stage {

    private ResourceBundle bundle;
    private TextField fieldUsername;
    private PasswordField fieldPassword;
    private Button buttonLogin;

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
        Scene scene = new Scene(root, 350, 450);
        setScene(scene);
        setResizable(false);
        setTitle("OGS: Online-Go.com");
        UIStylesheets.applyTo(scene);
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

        buttonLogin = new Button(bundle.getString("login.submit"));
        buttonLogin.setOnAction(this::submitLogin);
        VBox.setMargin(buttonLogin, new Insets(25, 5, 5, 5));
        pane.getChildren().add(buttonLogin);

        return pane;
    }

    private void submitLogin(ActionEvent actionEvent) {
        //TODO work in progress

        String username = fieldUsername.getText();
        String password = fieldPassword.getText();

        System.out.println("Submitting action");
        // REST request to OGS
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost post = new HttpPost("http://online-go.com/oauth2/access_token");
            System.out.println("Building request");
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("client_id", Settings.ogsToken.getClientId()));
            parameters.add(new BasicNameValuePair("client_secret", Settings.ogsToken.getClientSecret()));
            parameters.add(new BasicNameValuePair("grant_type", "password"));
            parameters.add(new BasicNameValuePair("username", username));
            parameters.add(new BasicNameValuePair("password", password));
            post.setEntity(new UrlEncodedFormEntity(parameters));
            System.out.println("Executing request");
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            Header encodingHeader = entity.getContentEncoding();
            Charset encoding = encodingHeader == null ? StandardCharsets.UTF_8 : Charsets.toCharset(encodingHeader.getValue());
            String json = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println("Received:");
            System.out.println(response.getStatusLine().getStatusCode());

        } catch (IOException e) {
            // TODO error handling
            e.printStackTrace();
        }
    }

}
