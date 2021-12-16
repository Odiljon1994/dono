package com.centerprime.ttap.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;


import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.databinding.FragmentMainBinding;
import com.centerprime.ttap.databinding.FragmentWalletBinding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.Token;
import com.centerprime.ttap.ui.viewmodel.CoinMarketCapVM;
import com.centerprime.ttap.ui.viewmodel.NotificationVM;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;
    private String walletAddress;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    CoinMarketCapVM coinMarketCapVM;
    int tokenCount = 0;
    private List<Token> tokens = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        coinMarketCapVM = ViewModelProviders.of(this, viewModelFactory).get(CoinMarketCapVM.class);
        coinMarketCapVM.item().observe(getActivity(), this::onResponseCoinMarketCap);
        View view = binding.getRoot();


        tokens.add(new Token("ETH", 0, 0));
        tokens.add(new Token("BNB", 0, 0));
        tokens.add(new Token("TTAP", 0, 0));

        walletAddress = preferencesUtil.getWalletAddress();
        binding.walletAddress.setText(preferencesUtil.getWalletAddress());
        binding.copyBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", binding.walletAddress.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Copied!", Toast.LENGTH_SHORT).show();
        });

        checkBalance();
        checkEthBalance();
        checkBalanceBnb();

        binding.ttapAsset.setOnClickListener(v -> {
            Fragment someFragment = new TokenFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack

            Bundle bundle = new Bundle();
            bundle.putString("tokenName", "TTAP");
            bundle.putString("contractAddress", ApiUtils.getContractAddress());
            someFragment.setArguments(bundle);


            transaction.commit();
        });

        binding.ethAsset.setOnClickListener(v -> {
            Fragment someFragment = new TokenFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack

            Bundle bundle = new Bundle();
            bundle.putString("tokenName", "ETH");
            bundle.putString("contractAddress", preferencesUtil.getWalletAddress());
            someFragment.setArguments(bundle);

            transaction.commit();
        });

        binding.bnb.setOnClickListener(v -> {
            Fragment someFragment = new TokenFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack

            Bundle bundle = new Bundle();
            bundle.putString("tokenName", "BNB");
            bundle.putString("contractAddress", ApiUtils.getBnbContractAddress());
            someFragment.setArguments(bundle);

            transaction.commit();
        });
        return view;

    }

    public void checkEthBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.balanceInEth(walletAddress, getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    binding.amountEth.setText(balance.toString());
                    tokens.get(0).setTokenAmount(Double.parseDouble(balance.toString()));
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                }, error -> {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                });
    }


    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.getTokenBalance(walletAddress, "", ApiUtils.getContractAddress(), getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    binding.amountTtap.setText(balance.toString());
                    tokens.get(2).setTokenAmount(Double.parseDouble(balance.toString()));
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                }, error -> {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                });
    }

    public void checkBalanceBnb() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.getTokenBalance(walletAddress, "", ApiUtils.getBnbContractAddress(), getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    binding.amountBnb.setText(balance.toString());
                    tokens.get(1).setTokenAmount(Double.parseDouble(balance.toString()));
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                }, error -> {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                });
    }

    public void onResponseCoinMarketCap(List<Token> tokens) {

        String ethKrw = String.format("%,.2f", tokens.get(0).getAmountInKrw());
        String bnbKrw = String.format("%,.2f", tokens.get(1).getAmountInKrw());
        String ttapKrw = String.format("%,.2f", tokens.get(2).getAmountInKrw());
        
        binding.ethKrw.setText(ethKrw + " KRW");
        binding.bnbKrw.setText(bnbKrw + " KRW");
        binding.ttapKrw.setText(ttapKrw + " KRW");

    }

}
