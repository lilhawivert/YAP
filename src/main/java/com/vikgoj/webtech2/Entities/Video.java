package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "videos")
public class Video {

    @Column
    private String username;

    @Id
    private String url;

    @Column
    private int points;

    @Override
    public String toString() {
        return "Video [username=" + username + ", url=" + url + ", points=" + points + "]";
    }

    public Video(String username, String url, int points) {
        this.username = username;
        this.url = url;
        this.points = points;
    }

    public Video() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
