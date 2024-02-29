package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private Long yapId;

    @Column
    private String userThatLiked;

    public Long getYapId() {
        return yapId;
    }

    public void setYapId(Long yapId) {
        this.yapId = yapId;
    }

    public String getUserThatLiked() {
        return userThatLiked;
    }

    public void setUserThatLiked(String userThatLiked) {
        this.userThatLiked = userThatLiked;
    }

    public Like() {
    }

    public Like(Long yapId, String userThatLiked) {
        this.yapId = yapId;
        this.userThatLiked = userThatLiked;
    }

    
}
