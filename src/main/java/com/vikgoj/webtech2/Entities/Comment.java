package com.vikgoj.webtech2.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String message;
    @Column
    private Integer likes = 0;
    @Column
    private Boolean deleted;
    @Column
    private Boolean liked;
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonBackReference
    private Yap yap;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getMessage() {
        return message;
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getLiked() {
        return liked;
    }
    public void setLiked(Boolean liked) {
        this.liked = liked;
    }
    public Integer getLikes() {
        return likes;
    }
    public void setLikes(Integer likes) {
        this.likes = likes;
    }
    public Yap getYap() {
        return yap;
    }
    public void setYap(Yap yap) {
        this.yap = yap;
    }
    public Comment(Long id, String username, String message, Integer likes, Yap yap) {
        this.id = id;
        this.username = username;
        this.message = message;
        this.likes = likes;
        this.yap = yap;
    }
    public Comment() {
    }
}
