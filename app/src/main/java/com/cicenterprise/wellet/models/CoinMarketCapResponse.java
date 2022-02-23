package com.cicenterprise.wellet.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinMarketCapResponse {

    @SerializedName("data")
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("symbol")
        String symbol;
        @SerializedName("quote")
        Quote quote;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Quote getQuote() {
            return quote;
        }

        public void setQuote(Quote quote) {
            this.quote = quote;
        }
    }

    public class Quote{
        @SerializedName("KRW")
        private BNB bnb;

        public BNB getBnb() {
            return bnb;
        }

        public void setBnb(BNB bnb) {
            this.bnb = bnb;
        }
    }

    public class BNB{
        @SerializedName("price")
        private String price;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
