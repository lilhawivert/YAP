package com.vikgoj.webtech2.Entities;

import java.sql.Time;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;

    @Column
    private String oAuth;

    @Column
    private boolean enabled;

    @Column
    private int points;

    

    @Column
    private java.sql.Time oauthTime;

    public String getoAuth() {
        return oAuth;
    }
    /*
     * Returns true if OAuth is existant and the timestamp is less than an hour old.
     */
    public boolean isOauthValid() {
        if(oAuth == null) return false;
        Time currentTime = java.sql.Time.valueOf(LocalTime.now());
        return Math.abs(currentTime.getTime() - oauthTime.getTime()) <= 360000;
    }

    public User(String username, String oAuth, boolean enabled, Time oauthTime, String password, int points) {
        this.username = username;
        this.oAuth = oAuth;
        this.enabled = enabled;
        this.oauthTime = oauthTime;
        this.password = password;
        this.points = points;
    }

    public java.sql.Time getOauthTime() {
        return oauthTime;
    }

    public void setOauthTime(java.sql.Time oauthTime) {
        this.oauthTime = oauthTime;
    }

    public void setoAuth(String oAuth) {
        this.oAuth = oAuth;
    }

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Column
    private String password;

    @Override
    public String toString() {
        return "User [username=" + username + ", password=" + password + "]";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

}
