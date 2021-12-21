package com.centerprime.ttap.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centerprime.ttap.api.Api;
import com.centerprime.ttap.models.PrivacyPolicyModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TermsOfUseVM extends BaseVM{
    private Api api;
    private Context context;
    private MutableLiveData<PrivacyPolicyModel> privacyPolicyModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();
    @Inject
    public TermsOfUseVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<PrivacyPolicyModel> item() {
        return privacyPolicyModelMutableLiveData;
    }

    public LiveData<String> itemOnError() {
        return onError;
    }

    public void getTermsOfUse() {

        addToSubscribe(api.getTermsOfUse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    privacyPolicyModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
