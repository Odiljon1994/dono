package com.mcs.wallet.api;

import com.mcs.wallet.models.EtherscnaResponce;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanAPI {

    @GET("/api?module=account&action=txlist&startblock=0&endblock=99999999&sort=desc&apikey=GBVTPKAIJVSFX1WBU698WH83XSNFQ9PKDC/")
    Single<EtherscnaResponce> getTransactrions(@Query("address") String address);

    @GET("/api?module=account&action=tokentx&startblock=0&endblock=99999999&sort=desc&apikey=GBVTPKAIJVSFX1WBU698WH83XSNFQ9PKDC/")
    Single<EtherscnaResponce> getERC20Transactrions(@Query("address") String address, @Query("contractaddress") String contractAddress);
}
