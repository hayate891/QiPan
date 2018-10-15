package io.nibby.qipan.ogs;

public class OgsPlayer {

    private boolean professional = false;
    private boolean ai = false;
    private boolean aiOwner = false;
    private boolean supporter = false;

    private int rating;
    private int rank;
    private String username;
    private int id;

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
