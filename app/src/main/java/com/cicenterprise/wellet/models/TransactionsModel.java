package com.cicenterprise.wellet.models;

import com.google.gson.annotations.SerializedName;

public class TransactionsModel implements Comparable<TransactionsModel> {
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
    @SerializedName("blockNumber")
    String blockNumber;
    @SerializedName("blockHash")
    String blockHash;
    @SerializedName("tokenName")
    String tokenName;
    @SerializedName("tokenSymbol")
    String tokenSymbol;
    @SerializedName("contractAddress")
    String contractAddress;
    @SerializedName("tokenDecimal")
    String tokenDecimal;

    public String getTokenDecimal() {
        return tokenDecimal;
    }

    public void setTokenDecimal(String tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
    }

    String type;

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public TransactionsModel(Long timeStamp,
                             String hash,
                             String value,
                             String isError,
                             String from,
                             String to,
                             String blockNumber,
                             String blockHash,
                             String type) {
        this.timeStamp = timeStamp;
        this.hash = hash;
        this.value = value;
        this.isError = isError;
        this.from = from;
        this.to = to;
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.type = type;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

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

    public TransactionsModel(Long timeStamp,
                             String hash,
                             String value,
                             String isError,
                             String from,
                             String to,
                             String blockNumber,
                             String blockHash,
                             String tokenName,
                             String tokenSymbol,
                             String contractAddress,
                             String tokenDecimal,
                             String type) {
        this.timeStamp = timeStamp;
        this.hash = hash;
        this.value = value;
        this.isError = isError;
        this.from = from;
        this.to = to;
        this.blockNumber = blockNumber;
        this.blockHash = blockHash;
        this.tokenName = tokenName;
        this.tokenSymbol = tokenSymbol;
        this.contractAddress = contractAddress;
        this.tokenDecimal = tokenDecimal;
        this.type = type;
    }

    @Override
    public int compareTo(TransactionsModel o) {
        return getTimeStamp().compareTo(o.getTimeStamp());
    }
}
