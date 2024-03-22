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
    private String userWhosBlocked;
    @Column
    private String userWhoBlocks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserWhosBlocked() {
        return userWhosBlocked;
    }

    public void setUserWhosFollowed(String userwhosblocked) {
        this.userWhosBlocked = userwhosblocked;
    }



    public Block() {
    }

    public Block(String userwhosblocked, String userWhoblocks) {
        this.userWhosBlocked = userwhosblocked;
        this.userWhoBlocks = userWhoblocks;
    }

    public String getUserWhoBlocks() {
        return userWhoBlocks;
    }

    public void setUserWhoBlocks(String userWhoblocks) {
        this.userWhoBlocks = userWhoblocks;
    }

}