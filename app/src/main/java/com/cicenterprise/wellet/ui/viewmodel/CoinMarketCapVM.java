package com.cicenterprise.wellet.ui.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cicenterprise.wellet.api.CoinMarketCapAPI;
import com.cicenterprise.wellet.models.Token;
import com.cicenterprise.wellet.models.TokensModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CoinMarketCapVM extends BaseVM{
    private CoinMarketCapAPI coinMarketCapAPI;
    private Context context;
    private MutableLiveData<List<Token>> tokenMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<TokensModel>> tokenMutableLiveData2 = new MutableLiveData<>();
    private List<Token> model = new ArrayList<>();

    @Inject
    public CoinMarketCapVM(CoinMarketCapAPI coinMarketCapAPI, Context context) {
        this.coinMarketCapAPI = coinMarketCapAPI;
        this.context = context;
    }

    public LiveData<List<Token>> item() {
        return tokenMutableLiveData;
    }

    public LiveData<List<TokensModel>> item2() {
        return tokenMutableLiveData2;
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


    public void getPrices2(List<TokensModel> tokens) {

        addToSubscribe(coinMarketCapAPI.getCoinPrices()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {

                    for (int i = 0; i < tokens.size(); i++) {
                        for (int j = 0; j < response.getData().size(); j++) {
                            if (tokens.get(i).getTokenSymbol().equals(response.getData().get(j).getSymbol())) {
                                Double tokenAmount = Double.parseDouble(tokens.get(i).getTokenAmount());
                                Double tokenAmountInWon = tokenAmount * Double.parseDouble(response.getData().get(j).getQuote().getBnb().getPrice());
                                tokens.get(i).setAmountInWon(String.format("%,.2f", tokenAmountInWon));
                                break;
                            }
                        }
                    }
                    tokenMutableLiveData2.postValue(tokens);

                    //   tokenMutableLiveData.postValue(response);

                }, error -> {
                    //onError.postValue(error.getMessage());
                    System.out.println(error.getMessage());
                }));
    }
}
