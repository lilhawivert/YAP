package com.vikgoj.webtech2.Entities;

public class OutgoingMessage {
    private String msg;
    private String from;
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OutgoingMessage() {
    }

    public OutgoingMessage(String msg, String from) {
        this.msg = msg;
        this.from = from;
    }
    
    
}
