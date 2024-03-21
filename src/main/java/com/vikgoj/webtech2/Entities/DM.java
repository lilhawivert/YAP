package com.vikgoj.webtech2.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "dms")
public class DM implements Comparable<DM> {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @Column
    private String sender;
    @Column
    private String message;
    @Column
    private String receiver;
    @Column
    private LocalDateTime date;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSender() {
        return sender;
    }
    public LocalDateTime  getDate() {
        return date;
    }
    public void setDate(LocalDateTime  date) {
        this.date = date;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public DM(String sender, String message, String receiver) {
        this.sender = sender;
        this.message = message;
        this.receiver = receiver;
    }
    public DM() {}

    @PrePersist
    protected void onCreate() {
        this.date = LocalDateTime.now();
    }
    @Override
    public int compareTo(DM o) {
        return getDate().compareTo(o.getDate());
    }

}
