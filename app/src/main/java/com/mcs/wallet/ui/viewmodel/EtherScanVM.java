package com.mcs.wallet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mcs.wallet.api.EtherscanAPI;
import com.mcs.wallet.models.TransactionsModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class EtherScanVM extends BaseVM{

    EtherscanAPI etherscanAPI;
    private MutableLiveData<List<TransactionsModel>> transactions = new MutableLiveData<>();
    private MutableLiveData<String> onNoTransaction = new MutableLiveData<>();

    Context context;

    @Inject
    public EtherScanVM(EtherscanAPI etherscanAPI, Context context) {
        this.etherscanAPI = etherscanAPI;
        this.context = context;
    }

    public LiveData<List<TransactionsModel>> transactions() {
        return transactions;
    }

    public LiveData<String> noTransaction() {
        return onNoTransaction;
    }


    /**
     * @param ethAddress - getTransactionsList
     */

    // ---> TestNet transactions <---
    public void getTransactions(String ethAddress) {

        List<TransactionsModel> transactionModels = new ArrayList<>();
        addToSubscribe(etherscanAPI.getTransactrions(ethAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions1 -> {

                    if (transactions1.getStatus().equals("1")) {
                        for (int i = 0; i < transactions1.getResult().size(); i++) {

                            transactionModels.add(new TransactionsModel(transactions1.getResult().get(i).getTimeStamp(),
                                    transactions1.getResult().get(i).getHash(),
                                    transactions1.getResult().get(i).getValue(),
                                    transactions1.getResult().get(i).getIsError(),
                                    transactions1.getResult().get(i).getFrom(),
                                    transactions1.getResult().get(i).getTo(),
                                    transactions1.getResult().get(i).getBlockNumber(),
                                    transactions1.getResult().get(i).getBlockHash(), "MATIC"));
                        }
                    }
                    getERC20Transactions(ethAddress, transactionModels);

                }, error -> {
                    getERC20Transactions(ethAddress, transactionModels);
                    System.out.println(error.getMessage());
                }));
    }

    public void getERC20Transactions(String ethAddress, List<TransactionsModel> transactionModels) {
        addToSubscribe(etherscanAPI.getERC20Transactrions(ethAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(erc20Transactions -> {
                    if (erc20Transactions.getStatus().equals("1")) {
                        for (int i = 0; i < erc20Transactions.getResult().size(); i++) {
                            transactionModels.add(new TransactionsModel(erc20Transactions.getResult().get(i).getTimeStamp(),
                                    erc20Transactions.getResult().get(i).getHash(),
                                    erc20Transactions.getResult().get(i).getValue(),
                                    erc20Transactions.getResult().get(i).getIsError(),
                                    erc20Transactions.getResult().get(i).getFrom(),
                                    erc20Transactions.getResult().get(i).getTo(),
                                    erc20Transactions.getResult().get(i).getBlockNumber(),
                                    erc20Transactions.getResult().get(i).getBlockHash(),
                                    erc20Transactions.getResult().get(i).getTokenName(),
                                    erc20Transactions.getResult().get(i).getTokenSymbol(),
                                    erc20Transactions.getResult().get(i).getContractAddress(),
                                    erc20Transactions.getResult().get(i).getTokenDecimal(),
                                    erc20Transactions.getResult().get(i).getTokenSymbol()));
                        }
                    }
                    transactions.postValue(transactionModels);
                    // getBEP20Transactions(bscAddress, transactionModels);
                }, error -> {
                    transactions.postValue(transactionModels);
                    // getBEP20Transactions(bscAddress, transactionModels);
                    System.out.println(error.getMessage());
                }));
    }
}
