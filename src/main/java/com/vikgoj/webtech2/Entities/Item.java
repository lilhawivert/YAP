package com.vikgoj.webtech2.Entities;

public class Item {
    private String videoId;
    private String rating;
    public String getVideoId() {
        return videoId;
    }
    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public Item(String videoId, String rating) {
        this.videoId = videoId;
        this.rating = rating;
    }
    public Item() {
    }
    @Override
    public String toString() {
        return "Item [videoId=" + videoId + ", rating=" + rating + "]";
    }

    
}
