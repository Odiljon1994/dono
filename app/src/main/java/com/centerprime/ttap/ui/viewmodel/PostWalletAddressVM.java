package com.centerprime.ttap.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centerprime.ttap.api.Api;
import com.centerprime.ttap.models.CurrentFeeModel;
import com.centerprime.ttap.models.PostWalletAddressReqModel;
import com.centerprime.ttap.models.PostWalletAddressResModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostWalletAddressVM extends BaseVM {

    private Api api;
    private Context context;
    private MutableLiveData<PostWalletAddressResModel> postWalletAddressResModelMutableLiveData = new MutableLiveData<>();
    private PostWalletAddressResModel model = new PostWalletAddressResModel();

    @Inject
    public PostWalletAddressVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<PostWalletAddressResModel> item() {
        return postWalletAddressResModelMutableLiveData;
    }

    public void postWalletAddress(String walletAddress) {

        PostWalletAddressReqModel postWalletAddressReqModel = new PostWalletAddressReqModel();
        postWalletAddressReqModel.setWalletAddress(walletAddress);
        addToSubscribe(api.postWalletAddress(postWalletAddressReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    System.out.println(response.getCode());
                    postWalletAddressResModelMutableLiveData.postValue(response);

                }, error -> {
                    System.out.println(error.getMessage());
                }));
    }

}
