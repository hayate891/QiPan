package io.nibby.qipan.settings;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class OgsAuthSettings implements SettingsModule {

    private static final Path FILE_PATH = Settings.BASE_DIRECTORY.resolve("ogs_token");

    private boolean exists = false;
    private String authToken;
    private String reAuthToken;

    private String clientId;
    private String clientSecret;

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(FILE_PATH))) {
            writer.println(authToken);
            writer.println(reAuthToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        try {
            Scanner scan = new Scanner(OgsAuthSettings.class.getResourceAsStream("/ogs-client.txt"));
            clientId = scan.nextLine().split(":")[1];
            clientSecret = scan.nextLine().split(":")[1];
            scan.close();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Missing ogs client configuration!");
            alert.setContentText("The OGS client feature will not be available.");
            alert.showAndWait();
        }

        exists = Files.exists(FILE_PATH);
        if (!exists)
            return;

        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
            authToken = reader.readLine();
            reAuthToken = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean tokenExists() {
        return exists;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getReAuthToken() {
        return reAuthToken;
    }

    public void setReAuthToken(String reAuthToken) {
        this.reAuthToken = reAuthToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
