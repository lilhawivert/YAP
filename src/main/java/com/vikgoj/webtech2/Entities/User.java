package com.vikgoj.webtech2.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String username;
    @Column
    private String password;
    @Column(length = 64000)
    private String profilePics;
    @Column
    private Long bgColor;
    
    public User(Long id,String username, String password, String profilePic, Long bgColor) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.profilePics = profilePic;
        this.bgColor = bgColor;
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
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilePic() {
        return profilePics;
    }
    public void setProfilePic(String profilePic) {
        this.profilePics = profilePic;
    }

    public Long getBgColor() {
        return bgColor;
    }

    public void setBgColor(Long bgColor) {
        this.bgColor = bgColor;
    }
}
