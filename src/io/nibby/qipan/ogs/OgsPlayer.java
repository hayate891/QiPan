package io.nibby.qipan.ogs;

import javafx.scene.image.Image;
import org.json.JSONObject;

public class OgsPlayer {

    private boolean professional = false;
    private boolean ai = false;
    private boolean aiOwner = false;
    private boolean supporter = false;
    private Image icon = null;

    private int rating;
    private int rank;
    private String username;
    private int id;

    public static OgsPlayer parseBriefly(JSONObject playerData) {
        OgsPlayer player = new OgsPlayer();
        JSONObject r = playerData.getJSONObject("ratings").getJSONObject("overall");
        player.rating = (int) Math.round(r.getDouble("rating"));
        player.rank = playerData.getInt("rank");
        player.id = playerData.getInt("id");
        player.username = playerData.getString("username");
        player.professional = playerData.getBoolean("professional");

        return player;
    }

    public static OgsPlayer parse(JSONObject playerData) {
        System.out.println(playerData);
        OgsPlayer player = new OgsPlayer();
        player.id = playerData.getInt("id");
        player.username = playerData.getString("username");
        player.professional = playerData.getBoolean("professional");
//        player.ai = playerData.getBoolean("bot_ai");
        // TODO aiOwner
        player.supporter = playerData.getBoolean("supporter");

        String iconUrl = playerData.getString("icon");
        player.icon = new Image(iconUrl, 64, 64, false, true, true);
        // TODO parse player rating + rank
        JSONObject ratings = playerData.getJSONObject("ratings");
        JSONObject ratingsOverall = ratings.getJSONObject("overall");
        player.rating = (int) Math.round(ratingsOverall.getDouble("rating"));

        return player;
    }

    public static String getRankText(OgsPlayer player) {
        boolean isDan = player.rank > 30;
        String postfix = "";
        if (player.professional)
            postfix = "p";
        else if (isDan)
            postfix = "d";
        else
            postfix = "k";

        int value = isDan ? (player.rank - 30) + 1 : 30 - player.rank;
        System.out.println("Parse as: " + value + postfix);
        return value + postfix;
    }

    public int getRank() {
        return rank;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Image getIcon() {
        return icon;
    }
}
