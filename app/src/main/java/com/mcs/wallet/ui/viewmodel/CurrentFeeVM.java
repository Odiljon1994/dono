package com.mcs.wallet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mcs.wallet.api.Api;
import com.mcs.wallet.models.CurrentFeeModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CurrentFeeVM extends BaseVM {

    private Api api;
    private Context context;
    private MutableLiveData<CurrentFeeModel> currentFeeModelMutableLiveData = new MutableLiveData<>();
    private CurrentFeeModel model = new CurrentFeeModel();

    @Inject
    public CurrentFeeVM(Api api, Context context) {
        this.api = api;
        this.context = context;
    }

    public LiveData<CurrentFeeModel> item() {
        return currentFeeModelMutableLiveData;
    }

    public void getCurrentFee() {

        addToSubscribe(api.getCurrentFee()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    model.setFee(response.getFee());
                    model.setType(response.getType());
                    model.setUpdated_at(response.getUpdated_at());
                    currentFeeModelMutableLiveData.postValue(model);

                }, error -> {
                    System.out.println(error.getMessage());
                }));
    }
}
