package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "blocks")

public class Block {


@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String userwhosblocked;
    @Column
    private String userWhoblocks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserWhosBlocked() {
        return userwhosblocked;
    }

    public void setUserWhosFollowed(String userwhosblocked) {
        this.userwhosblocked = userwhosblocked;
    }

    

    public Block() {
    }

    public Block(String userwhosblocked, String userWhoblocks) {
        this.userwhosblocked = userwhosblocked;
        this.userWhoblocks = userWhoblocks;
    }

    public String getUserWhoBlocks() {
        return userWhoblocks;
    }

    public void setUserWhoBlocks(String userWhoblocks) {
        this.userWhoblocks = userWhoblocks;
    }

}
