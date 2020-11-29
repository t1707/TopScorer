package com.example.restservice;

public class PageRequestPayload {
    private Integer page = 0;
    private Integer size = 3;
    private String sortId = "id";
    private String before = "9999-99-99 00:00:00";
    private String after = "0000-00-00 00:00:00";
    private String players = "";

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getSortId() {
        return sortId;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getBefore() {
        return before;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getAfter() {
        return after;
    }

    public void setPlayers(String players) {
        this.players = players;
    }

    public String getPlayers() {
        return players;
    }
}
