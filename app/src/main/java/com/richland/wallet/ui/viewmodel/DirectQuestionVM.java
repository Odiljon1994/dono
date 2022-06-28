package com.richland.wallet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.richland.wallet.api.Api;
import com.richland.wallet.models.DirectQuestionReqModel;
import com.richland.wallet.models.DirectQuestionResModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DirectQuestionVM extends BaseVM {

    private Api api;
    private Context context;
    private MutableLiveData<DirectQuestionResModel> directQuestionResModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();

    @Inject
    public DirectQuestionVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<DirectQuestionResModel> item() {
        return directQuestionResModelMutableLiveData;
    }

    public LiveData<String> itemOnError() {
        return onError;
    }

    public void postQuestion(String email, String title, String message) {

        DirectQuestionReqModel directQuestionReqModel = new DirectQuestionReqModel();
        directQuestionReqModel.setEmail(email);
        directQuestionReqModel.setTitle(title);
        directQuestionReqModel.setMessage(message);
        addToSubscribe(api.directQuestion(directQuestionReqModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    directQuestionResModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }

}
