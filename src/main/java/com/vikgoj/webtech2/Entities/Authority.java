package com.vikgoj.webtech2.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="authorities")
public class Authority {

    @Id
    private String username;

    @Column
    private String authority;

    public Authority(String username, String role) {
        this.username = username;
        this.authority = role;
    }

    public Authority() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String role) {
        this.authority = role;
    }


        
}
