package com.centerprime.ttap.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EtherscnaResponce {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    List<Transaction> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Transaction> getResult() {
        return result;
    }

    public void setResult(List<Transaction> result) {
        this.result = result;
    }
}
