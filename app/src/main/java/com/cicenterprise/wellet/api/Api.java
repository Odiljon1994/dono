package com.cicenterprise.wellet.api;

import com.cicenterprise.wellet.models.BannerListModel;
import com.cicenterprise.wellet.models.CurrentFeeModel;
import com.cicenterprise.wellet.models.DirectQuestionReqModel;
import com.cicenterprise.wellet.models.DirectQuestionResModel;
import com.cicenterprise.wellet.models.FaqModel;
import com.cicenterprise.wellet.models.NotificationModel;
import com.cicenterprise.wellet.models.PostTransactionReqModel;
import com.cicenterprise.wellet.models.PostTransactionResModel;
import com.cicenterprise.wellet.models.PostWalletAddressReqModel;
import com.cicenterprise.wellet.models.PostWalletAddressResModel;
import com.cicenterprise.wellet.models.PrivacyPolicyModel;

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


    @GET("/getEvents")
    Single<NotificationModel> getNotifications();

    @POST("/createTransaction")
    Single<PostTransactionResModel> postTransaction(@Body PostTransactionReqModel postTransactionReqModel);

    @GET("/getFaq")
    Single<FaqModel> getFaq();

    @GET("/versionListPrivacy")
    Single<PrivacyPolicyModel> getPrivacyPolicy();

    @GET("/versionListTerm")
    Single<PrivacyPolicyModel> getTermsOfUse();

    @GET("/bannerList")
    Single<BannerListModel> getBannerList();

}
