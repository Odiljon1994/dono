package com.richland.wallet.web3;

public class MnemonicWallet {
    private String mnemonic;
    private String privateKey;
    private String walletAddress;

    public MnemonicWallet(String mnemonic, String privateKey, String walletAddress) {
        this.mnemonic = mnemonic;
        this.privateKey = privateKey;
        this.walletAddress = walletAddress;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getWalletAddress() {
        return walletAddress;
    }
}
