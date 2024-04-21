package com.mcs.wallet.api;

import com.mcs.wallet.models.CoinMarketCapResponse;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface CoinMarketCapAPI {
    @GET("latest?CMC_PRO_API_KEY=7433b1d8-24cb-484d-9df6-114a6067c571&start=1&limit=200&convert=KRW")
    Single<CoinMarketCapResponse> getCoinPrices();
}
