package com.richland.wallet.models;

import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("timeStamp")
    Long timeStamp;
    @SerializedName("hash")
    String hash;
    @SerializedName("value")
    String value;
    @SerializedName("isError")
    String isError;
    @SerializedName("from")
    String from;
    @SerializedName("to")
    String to;

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsError() {
        return isError;
    }

    public void setIsError(String isError) {
        this.isError = isError;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


    public Transaction(Long timeStamp, String hash, String value, String isError, String from, String to) {
        this.timeStamp = timeStamp;
        this.hash = hash;
        this.value = value;
        this.isError = isError;
        this.from = from;
        this.to = to;
    }

}
