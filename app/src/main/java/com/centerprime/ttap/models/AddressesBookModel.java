package com.centerprime.ttap.models;

public class AddressesBookModel {
    private String name;
    private String walletAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressesBookModel(String name, String walletAddress) {
        this.name = name;
        this.walletAddress = walletAddress;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}
