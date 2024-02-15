package com.vikgoj.webtech2.Entities;
import java.util.Arrays;
import java.util.HashMap;

public class LikeResponse {

    private String kind;
    private String etag;
    private Item[] items;
    public String getKind() {
        return kind;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }
    public String getEtag() {
        return etag;
    }
    public void setEtag(String etag) {
        this.etag = etag;
    }
    public Item[] getItems() {
        return items;
    }
    public void setItems(Item[] items) {
        this.items = items;
    }
    public LikeResponse() {
    }
    public LikeResponse(String kind, String etag, Item[] items) {
        this.kind = kind;
        this.etag = etag;
        this.items = items;
    }
    @Override
    public String toString() {
        return "LikeResponse [kind=" + kind + ", etag=" + etag + ", items=" + Arrays.toString(items) + "]";
    }
    
    

    

}
