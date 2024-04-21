package com.mcs.wallet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mcs.wallet.api.Api;
import com.mcs.wallet.models.FaqModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FaqVM extends BaseVM{

    private Api api;
    private Context context;
    private MutableLiveData<FaqModel> faqModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();
    @Inject

    public FaqVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<FaqModel> item() {
        return faqModelMutableLiveData;
    }

    public LiveData<String> itemOnError() {
        return onError;
    }

    public void getFaq() {

        addToSubscribe(api.getFaq()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    faqModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
