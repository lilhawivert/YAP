package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "follows")
public class Follow {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String userWhosFollowed;

    @Column
    private String userWhoFollows;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserWhosFollowed() {
        return userWhosFollowed;
    }

    public void setUserWhosFollowed(String userWhosFollowed) {
        this.userWhosFollowed = userWhosFollowed;
    }

    

    public Follow() {
    }

    public Follow(String userWhosFollowed, String userWhoFollows) {
        this.userWhosFollowed = userWhosFollowed;
        this.userWhoFollows = userWhoFollows;
    }

    public String getUserWhoFollows() {
        return userWhoFollows;
    }

    public void setUserWhoFollows(String userWhoFollows) {
        this.userWhoFollows = userWhoFollows;
    }
    

    
}
