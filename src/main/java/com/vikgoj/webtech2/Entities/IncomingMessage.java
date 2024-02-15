package com.vikgoj.webtech2.Entities;

public class IncomingMessage {
    private String message;
    private String username;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public IncomingMessage() {
    }
    public IncomingMessage(String message) {
        this.message = message;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    
}
