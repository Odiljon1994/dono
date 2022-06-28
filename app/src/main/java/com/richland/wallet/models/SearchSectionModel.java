package com.richland.wallet.models;

public class SearchSectionModel {
    private String type;
    private String title;
    private int id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SearchSectionModel(String section) {
        this.title = section;
    }

    public SearchSectionModel(String type, String title, int id) {
        this.type = type;
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
