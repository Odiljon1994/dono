package com.centerprime.ttap.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centerprime.ttap.api.Api;
import com.centerprime.ttap.models.BannerListModel;
import com.centerprime.ttap.models.DirectQuestionReqModel;
import com.centerprime.ttap.models.DirectQuestionResModel;
import com.centerprime.ttap.models.FaqModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BannersListVM extends BaseVM{
    private Api api;
    private Context context;
    private MutableLiveData<BannerListModel> bannerListModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();

    @Inject
    public BannersListVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<BannerListModel> item() {
        return bannerListModelMutableLiveData;
    }

    public LiveData<String> itemOnError() {
        return onError;
    }

    public void getBannerList() {

        addToSubscribe(api.getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    bannerListModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
