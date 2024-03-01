package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comment_likes")
public class CommentLike {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private Long commentId;

    @Column
    private String userThatLiked;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getUserThatLiked() {
        return userThatLiked;
    }

    public void setUserThatLiked(String userThatLiked) {
        this.userThatLiked = userThatLiked;
    }

    public CommentLike() {
    }

    public CommentLike(Long commentId, String userThatLiked) {
        // this.id = id;
        this.commentId = commentId;
        this.userThatLiked = userThatLiked;
    }
    
    

    
}
