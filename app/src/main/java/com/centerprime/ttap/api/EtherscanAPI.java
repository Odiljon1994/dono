package com.centerprime.ttap.api;

import com.centerprime.ttap.models.EtherscnaResponce;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanAPI {

    @GET("/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=desc&apikey=RGTV1D2EN6I95Z8YXB6I4TYGY5MYG9PEYD/")
    Single<EtherscnaResponce> getTransactrions(@Query("address") String address);
}