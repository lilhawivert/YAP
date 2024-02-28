package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "yaps")
public class Yap {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String message;
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
    public void setMessage(String message) {
        this.message = message;
    }
    public Yap() {
    }
    public Yap(Long id, String username, String message) {
        this.id = id;
        this.username = username;
        this.message = message;
    }

    
}
