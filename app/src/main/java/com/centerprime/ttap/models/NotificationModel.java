package com.centerprime.ttap.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel {

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private List<Data> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("type")
        private String type;
        @SerializedName("content")
        private String content;
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;

        public Data(int id, String name, String type, String content, String updated_at, String created_at) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.content = content;
            this.updated_at = updated_at;
            this.created_at = created_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
