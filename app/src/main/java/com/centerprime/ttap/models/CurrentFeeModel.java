package com.centerprime.ttap.models;

import com.google.gson.annotations.SerializedName;

public class CurrentFeeModel {
    @SerializedName("type")
    private String type;
    @SerializedName("fee")
    private double fee;
    @SerializedName("updated_at")
    private String updated_at;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
