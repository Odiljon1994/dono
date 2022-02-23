package com.cicenterprise.wellet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cicenterprise.wellet.api.EtherscanAPI;
import com.cicenterprise.wellet.models.Transaction;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EthereumVM extends BaseVM {
    EtherscanAPI etherscanAPI;
    private MutableLiveData<List<Transaction>> transactions = new MutableLiveData<>();



    Context context;

    @Inject
    public EthereumVM(EtherscanAPI etherscanAPI, Context context) {
        this.etherscanAPI = etherscanAPI;
        this.context = context;
    }

    public LiveData<List<Transaction>> transactions() {
        return transactions;
    }

    /**
     * @param address - getTransactionsList
     */
    public void getTransactions(String address) {

        List<Transaction> transactionModels = new ArrayList<>();
        addToSubscribe(etherscanAPI.getTransactrions( address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions1 -> {

                    if (transactions1.getStatus().equals("1")) {
                        for (int i = 0; i < transactions1.getResult().size(); i++) {

                            transactionModels.add(new Transaction(transactions1.getResult().get(i).getTimeStamp(),
                                    transactions1.getResult().get(i).getHash(),
                                    transactions1.getResult().get(i).getValue(),
                                    transactions1.getResult().get(i).getIsError(),
                                    transactions1.getResult().get(i).getFrom(),
                                    transactions1.getResult().get(i).getTo()));
                        }
                        transactions.postValue(transactionModels);
                    }

                }, error -> {
                    System.out.println("*error");
                    System.out.println(error.getMessage());
                }));
    }
}
