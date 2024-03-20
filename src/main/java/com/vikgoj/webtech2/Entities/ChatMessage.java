package com.vikgoj.webtech2.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats")

public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    private String content;

    private LocalDateTime timestamp;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getsender() {
        return sender;
    }
    public void setsender(String sender) {
        this.sender = sender;
    }

    public String getcontent() {
        return content;
    }
    public void setcontent(String content) {
        this.content = content;
    }


    public LocalDateTime gettimestamp() {
        return timestamp;
    }
    public void settimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }



    // Getters and setters
}