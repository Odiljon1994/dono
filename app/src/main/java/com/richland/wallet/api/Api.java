package com.richland.wallet.api;

import com.richland.wallet.models.BannerListModel;
import com.richland.wallet.models.CurrentFeeModel;
import com.richland.wallet.models.DirectQuestionReqModel;
import com.richland.wallet.models.DirectQuestionResModel;
import com.richland.wallet.models.FaqModel;
import com.richland.wallet.models.NotificationModel;
import com.richland.wallet.models.PostTransactionReqModel;
import com.richland.wallet.models.PostTransactionResModel;
import com.richland.wallet.models.PostWalletAddressReqModel;
import com.richland.wallet.models.PostWalletAddressResModel;
import com.richland.wallet.models.PrivacyPolicyModel;

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
