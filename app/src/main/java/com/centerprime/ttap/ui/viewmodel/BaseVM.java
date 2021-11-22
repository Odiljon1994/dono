package com.centerprime.ttap.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BaseVM extends ViewModel {

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public LiveData<String> errorMessage() {
        return errorMessage;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage.postValue(errorMessage);
    }

    void addToSubscribe(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }

}
