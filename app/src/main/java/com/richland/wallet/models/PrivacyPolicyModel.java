package com.richland.wallet.models;

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
        @SerializedName("updated_at")
        private String updated_at;
        @SerializedName("created_at")
        private String created_at;

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

        public PrivacyPolicyData(int id, String version, String content, String updated_at, String created_at) {
            this.id = id;
            this.version = version;
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
