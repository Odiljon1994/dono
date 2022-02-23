package com.cicenterprise.wellet.models;

import android.graphics.drawable.Drawable;

public class TokensModel {

    private Drawable logo;
    private String tokenName;
    private String tokenAmount;
    private String tokenSymbol;
    private String amountInWon;
    private String contractAddress;

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

    public String getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(String tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getAmountInWon() {
        return amountInWon;
    }

    public void setAmountInWon(String amountInWon) {
        this.amountInWon = amountInWon;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public TokensModel(Drawable logo, String tokenName, String tokenAmount, String tokenSymbol, String amountInWon, String contractAddress) {
        this.logo = logo;
        this.tokenName = tokenName;
        this.tokenAmount = tokenAmount;
        this.tokenSymbol = tokenSymbol;
        this.amountInWon = amountInWon;
        this.contractAddress = contractAddress;
    }
}
