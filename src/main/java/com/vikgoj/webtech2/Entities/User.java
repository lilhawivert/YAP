package com.vikgoj.webtech2.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    @Column
    private String password;
    @Column(length = 64000)
    private String profilePics;
    
    public User(String username, String password, String profilePic) {
        this.username = username;
        this.password = password;
        this.profilePics = profilePic;
    }

    public User() {}
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePics;
    }
    public void setProfilePic(String profilePic) {
        this.profilePics = profilePic;
    }
    
}
