package com.richland.wallet.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FaqModel {
    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private List<FaqData> data;
    @SerializedName("message")
    private String message;

    public List<FaqData> getData() {
        return data;
    }

    public void setData(List<FaqData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    public static class FaqData {
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("content")
        private String content;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("updated_at")
        private String updated_at;

        public FaqData(int id, String title, String content, String created_at, String updated_at) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
