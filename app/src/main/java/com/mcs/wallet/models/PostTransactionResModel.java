package com.mcs.wallet.models;

import com.google.gson.annotations.SerializedName;

public class PostTransactionResModel {
    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;

    public int getStatus() {
        return code;
    }

    public void setStatus(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
