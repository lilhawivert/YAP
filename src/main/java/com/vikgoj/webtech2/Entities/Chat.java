package com.vikgoj.webtech2.Entities;

import java.util.List;

public class Chat {
    
    private User user;
    private String lastDM; 
    
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getLastDM() {
        return lastDM;
    }

    public void setLastDM(String lastDM) {
        this.lastDM = lastDM;
    }

    public Chat() {}

    public Chat(User user, String lastDM) {
        this.user = user;
        this.lastDM = lastDM;
    }

    

}
