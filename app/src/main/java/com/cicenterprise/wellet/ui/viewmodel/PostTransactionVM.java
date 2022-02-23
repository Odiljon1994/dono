package com.cicenterprise.wellet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cicenterprise.wellet.api.Api;
import com.cicenterprise.wellet.models.PostTransactionReqModel;
import com.cicenterprise.wellet.models.PostTransactionResModel;

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

    public void postTransaction(String fee) {

        PostTransactionReqModel postTransactionReqModel = new PostTransactionReqModel();
        postTransactionReqModel.setFee(fee);

        addToSubscribe(api.postTransaction(postTransactionReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    System.out.println();
                    postTransactionResModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
