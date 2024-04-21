package com.mcs.wallet.models;

public class AddressesBookModel {
    private int ID;
    private String name;
    private String walletAddress;
    private boolean isChecked;

    public AddressesBookModel(int ID, String name, String walletAddress, boolean isChecked) {
        this.ID = ID;
        this.name = name;
        this.walletAddress = walletAddress;
        this.isChecked = isChecked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}
