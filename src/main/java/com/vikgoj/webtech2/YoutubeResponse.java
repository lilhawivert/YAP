package com.vikgoj.webtech2;

import java.util.Arrays;
import java.util.Map;

public class YoutubeResponse {
    private String etag;

    private Map<String, Integer> pageInfo;

    public YoutubeResponse() {
    }

    public Map<String, Integer> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(Map<String, Integer> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

}
