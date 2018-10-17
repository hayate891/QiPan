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
    private boolean tokenExpired = false;
    private String authToken;
    private String reAuthToken;
    private int tokenExpiry;
    private long tokenAcquireTime;
    private String tokenType;
    private String scope;
    private String lastUsername;

    private String clientId;
    private String clientSecret;

    @Override
    public void save() {
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(FILE_PATH))) {
            writer.println(authToken);
            writer.println(reAuthToken);
            writer.println(tokenExpiry);
            writer.println(tokenAcquireTime);
            writer.println(tokenType);
            writer.println(scope);
            writer.println(lastUsername);
            writer.flush();
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
            e.printStackTrace();
        }

        exists = Files.exists(FILE_PATH);
        if (!exists)
            return;

        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH)) {
            authToken = reader.readLine();
            reAuthToken = reader.readLine();
            tokenExpiry = Integer.parseInt(reader.readLine());
            tokenAcquireTime = Long.parseLong(reader.readLine());
            tokenType = reader.readLine();
            scope = reader.readLine();
            lastUsername = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tokenExpired = System.currentTimeMillis() - tokenAcquireTime > tokenExpiry;
    }

    public String getLastUsername() {
        return lastUsername;
    }

    public void setLastUsername(String lastUsername) {
        this.lastUsername = lastUsername;
    }

    public boolean tokenExists() {
        return exists;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
        if (authToken != null && !authToken.isEmpty())
            exists = true;
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

    public int getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(int tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
        this.tokenExpired = System.currentTimeMillis() - tokenAcquireTime > tokenExpiry;
    }

    public long getTokenAcquireTime() {
        return tokenAcquireTime;
    }

    public void setTokenAcquireTime(long tokenAcquireTime) {
        this.tokenAcquireTime = tokenAcquireTime;
        this.tokenExpired = System.currentTimeMillis() - tokenAcquireTime > tokenExpiry;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }
}
