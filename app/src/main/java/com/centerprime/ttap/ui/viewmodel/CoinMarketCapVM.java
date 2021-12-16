package com.centerprime.ttap.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.centerprime.ttap.api.CoinMarketCapAPI;
import com.centerprime.ttap.models.CurrentFeeModel;
import com.centerprime.ttap.models.Token;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CoinMarketCapVM extends BaseVM{
    private CoinMarketCapAPI coinMarketCapAPI;
    private Context context;
    private MutableLiveData<List<Token>> tokenMutableLiveData = new MutableLiveData<>();
    private List<Token> model = new ArrayList<>();

    @Inject
    public CoinMarketCapVM(CoinMarketCapAPI coinMarketCapAPI, Context context) {
        this.coinMarketCapAPI = coinMarketCapAPI;
        this.context = context;
    }

    public LiveData<List<Token>> item() {
        return tokenMutableLiveData;
    }

    public void getPrices(List<Token> tokens) {

        addToSubscribe(coinMarketCapAPI.getCoinPrices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    for (int i = 0; i < tokens.size(); i++) {
                        for (int j = 0; j < response.getData().size(); j++) {
                            if (tokens.get(i).getTokenSymbol().equals(response.getData().get(j).getSymbol())) {
                                Double tokenAmount = tokens.get(i).getTokenAmount();
                                Double tokenAmountInWon = tokenAmount * Double.parseDouble(response.getData().get(j).getQuote().getBnb().getPrice());
                                tokens.get(i).setAmountInKrw(tokenAmountInWon);
                                break;
                            }
                        }
                    }
                    tokenMutableLiveData.postValue(tokens);

                 //   tokenMutableLiveData.postValue(response);

                }, error -> {
                    //onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
