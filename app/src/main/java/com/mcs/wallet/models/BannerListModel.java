package com.mcs.wallet.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BannerListModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<BannerListData> data;

    public List<BannerListData> getData() {
        return data;
    }

    public void setData(List<BannerListData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class BannerListData {
        @SerializedName("id")
        private int id;
        @SerializedName("file_url")
        private String file_url;
        @SerializedName("position")
        private int position;
        @SerializedName("external_url")
        private String external_url;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("updated_at")
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getExternal_url() {
            return external_url;
        }

        public void setExternal_url(String external_url) {
            this.external_url = external_url;
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
