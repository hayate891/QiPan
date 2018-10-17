package io.nibby.qipan.ogs;

import org.json.JSONObject;

public class OgsPlayer {

    private boolean professional = false;
    private boolean ai = false;
    private boolean aiOwner = false;
    private boolean supporter = false;

    private int rating;
    private int rank;
    private String username;
    private int id;

    public static OgsPlayer parse(JSONObject playerData) {
        OgsPlayer player = new OgsPlayer();
        JSONObject r = playerData.getJSONObject("ratings").getJSONObject("overall");
        player.rating = (int) Math.round(r.getDouble("rating"));
        player.rank = playerData.getInt("rank");
        player.id = playerData.getInt("id");
        player.username = playerData.getString("username");
        player.professional = playerData.getBoolean("professional");

        return player;
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
}
