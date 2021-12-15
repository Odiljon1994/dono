package com.centerprime.ttap.api;

import com.centerprime.ttap.models.CurrentFeeModel;
import com.centerprime.ttap.models.DirectQuestionReqModel;
import com.centerprime.ttap.models.DirectQuestionResModel;
import com.centerprime.ttap.models.PostWalletAddressReqModel;
import com.centerprime.ttap.models.PostWalletAddressResModel;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    @GET("/getCurrentFee")
    Single<CurrentFeeModel> getCurrentFee();

    @POST("/createWallet")
    Single<PostWalletAddressResModel> postWalletAddress(@Body PostWalletAddressReqModel postWalletAddressReqModel);

    @POST("/sendMail")
    Single<DirectQuestionResModel> directQuestion(@Body DirectQuestionReqModel directQuestionReqModel);
}
