package com.centerprime.ttap.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centerprime.ttap.api.Api;
import com.centerprime.ttap.models.NotificationModel;
import com.centerprime.ttap.models.PostTransactionResModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PostTransactionVM extends BaseVM{

    private Api api;
    private Context context;

    private MutableLiveData<PostTransactionResModel> postTransactionResModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();

    @Inject
    public PostTransactionVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<PostTransactionResModel> item() {
        return postTransactionResModelMutableLiveData;
    }

    public LiveData<String> itemOnError() {
        return onError;
    }

    public void postTransaction() {

        addToSubscribe(api.postTransaction()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    postTransactionResModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
