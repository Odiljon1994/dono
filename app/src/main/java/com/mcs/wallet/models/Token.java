package com.mcs.wallet.models;

public class Token {
    private String tokenSymbol;
    private double tokenAmount;
    private double amountInKrw;

    public Token(String tokenSymbol, double tokenAmount, double amountInKrw) {
        this.tokenSymbol = tokenSymbol;
        this.tokenAmount = tokenAmount;
        this.amountInKrw = amountInKrw;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public double getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(double tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public double getAmountInKrw() {
        return amountInKrw;
    }

    public void setAmountInKrw(double amountInKrw) {
        this.amountInKrw = amountInKrw;
    }
}
