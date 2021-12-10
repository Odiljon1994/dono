package com.centerprime.ttap.api;

import com.centerprime.ttap.models.CurrentFeeModel;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface Api {

    @GET("/getCurrentFee")
    Single<CurrentFeeModel> getCurrentFee();
}
