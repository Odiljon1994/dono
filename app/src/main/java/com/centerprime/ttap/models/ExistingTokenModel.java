package com.centerprime.ttap.models;

import android.graphics.drawable.Drawable;

public class ExistingTokenModel {
    private Drawable logo;
    private String tokenName;
    private String tokenSymbol;
    private String contractAddress;

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public ExistingTokenModel(Drawable logo, String tokenName, String tokenSymbol, String contractAddress) {
        this.logo = logo;
        this.tokenName = tokenName;
        this.tokenSymbol = tokenSymbol;
        this.contractAddress = contractAddress;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}
