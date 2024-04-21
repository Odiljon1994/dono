package com.mcs.wallet.api;

import com.mcs.wallet.models.BannerListModel;
import com.mcs.wallet.models.CurrentFeeModel;
import com.mcs.wallet.models.DirectQuestionReqModel;
import com.mcs.wallet.models.DirectQuestionResModel;
import com.mcs.wallet.models.FaqModel;
import com.mcs.wallet.models.NotificationModel;
import com.mcs.wallet.models.PostTransactionReqModel;
import com.mcs.wallet.models.PostTransactionResModel;
import com.mcs.wallet.models.PostWalletAddressReqModel;
import com.mcs.wallet.models.PostWalletAddressResModel;
import com.mcs.wallet.models.PrivacyPolicyModel;

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
