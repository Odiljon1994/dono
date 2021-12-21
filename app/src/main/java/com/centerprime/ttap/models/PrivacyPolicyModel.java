package com.centerprime.ttap.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrivacyPolicyModel {

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private List<PrivacyPolicyData> data;
    @SerializedName("message")
    private String message;

    public List<PrivacyPolicyData> getData() {
        return data;
    }

    public void setData(List<PrivacyPolicyData> data) {
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



    public class PrivacyPolicyData {
        @SerializedName("id")
        private int id;
        @SerializedName("version")
        private String version;
        @SerializedName("content")
        private String content;


        public PrivacyPolicyData(int id, String title, String content) {
            this.id = id;
            this.version = title;
            this.content = content;

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }
}
