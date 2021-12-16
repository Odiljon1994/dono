package com.centerprime.ttap.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centerprime.ttap.api.Api;
import com.centerprime.ttap.models.CurrentFeeModel;
import com.centerprime.ttap.models.DirectQuestionReqModel;
import com.centerprime.ttap.models.NotificationModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NotificationVM extends BaseVM{

    private Api api;
    private Context context;
    private MutableLiveData<NotificationModel> notificationModelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();
    @Inject

    public NotificationVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<NotificationModel> item() {
        return notificationModelMutableLiveData;
    }

    public LiveData<String> itemOnError() {
        return onError;
    }

    public void getNotifications() {
        
        addToSubscribe(api.getNotifications()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    notificationModelMutableLiveData.postValue(response);

                }, error -> {
                    onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
