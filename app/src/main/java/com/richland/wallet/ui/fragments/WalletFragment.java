package com.richland.wallet.ui.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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


import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.api.ApiUtils;
import com.richland.wallet.databinding.FragmentMainBinding;
import com.richland.wallet.databinding.FragmentWalletBinding;
import com.richland.wallet.di.ViewModelFactory;
import com.richland.wallet.models.Token;
import com.richland.wallet.ui.AddTokenActivity;
import com.richland.wallet.ui.viewmodel.CoinMarketCapVM;
import com.richland.wallet.util.PreferencesUtil;
import com.richland.wallet.web3.EthManager;

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


        binding.addToken.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddTokenActivity.class)));
        tokens.add(new Token("ETH", 0, 0));
      //  tokens.add(new Token("BNB", 0, 0));
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
      //  checkBalanceBnb();

        binding.ttapAsset.setOnClickListener(v -> {
           // Fragment someFragment = new TokenFragment();
            Fragment someFragment = new TokensFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack

            String ttapKrw = String.format("%,.2f", tokens.get(1).getAmountInKrw());
            Bundle bundle = new Bundle();
            bundle.putString("tokenName", "TTAP");
            bundle.putString("KRW", ttapKrw);
            bundle.putString("contractAddress", ApiUtils.getContractAddress());
            someFragment.setArguments(bundle);


            transaction.commit();
        });

        binding.ethAsset.setOnClickListener(v -> {
            // Fragment someFragment = new TokenFragment();
            Fragment someFragment = new TokensFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack

            String ethKrw = String.format("%,.2f", tokens.get(0).getAmountInKrw());
            Bundle bundle = new Bundle();
            bundle.putString("tokenName", "ETH");
            bundle.putString("KRW", ethKrw);
            bundle.putString("contractAddress", preferencesUtil.getWalletAddress());
            someFragment.setArguments(bundle);

            transaction.commit();
        });

//        binding.bnb.setOnClickListener(v -> {
//            // Fragment someFragment = new TokenFragment();
//            Fragment someFragment = new TokensFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
//            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//
//            String bnbKrw = String.format("%,.2f", tokens.get(1).getAmountInKrw());
//            Bundle bundle = new Bundle();
//            bundle.putString("tokenName", "BNB");
//            bundle.putString("KRW", bnbKrw);
//            bundle.putString("contractAddress", ApiUtils.getBnbContractAddress());
//            someFragment.setArguments(bundle);
//
//            transaction.commit();
//        });
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
                    binding.progressBarEth.setVisibility(View.GONE);
                    binding.amountEth.setVisibility(View.VISIBLE);
                    tokens.get(0).setTokenAmount(Double.parseDouble(balance.toString()));
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                }, error -> {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBarEth.setVisibility(View.GONE);
                    binding.amountEth.setVisibility(View.VISIBLE);
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
                    binding.progressBarTtap.setVisibility(View.GONE);
                    binding.amountTtap.setVisibility(View.VISIBLE);
                    tokens.get(1).setTokenAmount(Double.parseDouble(balance.toString()));
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                }, error -> {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                    binding.progressBarTtap.setVisibility(View.GONE);
                    binding.amountTtap.setVisibility(View.VISIBLE);
                    tokenCount++;
                    if (tokenCount == tokens.size()) {
                        coinMarketCapVM.getPrices(tokens);
                    }
                });
    }

//    public void checkBalanceBnb() {
//
//        EthManager ethManager = EthManager.getInstance();
//        ethManager.init(ApiUtils.getInfura());
//
//        ethManager.getTokenBalance(walletAddress, "", ApiUtils.getBnbContractAddress(), getActivity())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(balance -> {
//                    binding.amountBnb.setText(balance.toString());
//                    binding.progressBarBnb.setVisibility(View.GONE);
//                    binding.amountBnb.setVisibility(View.VISIBLE);
//                    tokens.get(1).setTokenAmount(Double.parseDouble(balance.toString()));
//                    tokenCount++;
//                    if (tokenCount == tokens.size()) {
//                        coinMarketCapVM.getPrices(tokens);
//                    }
//                }, error -> {
//                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                    System.out.println(error.getMessage());
//                    tokenCount++;
//                    binding.progressBarBnb.setVisibility(View.GONE);
//                    binding.amountBnb.setVisibility(View.VISIBLE);
//                    if (tokenCount == tokens.size()) {
//                        coinMarketCapVM.getPrices(tokens);
//                    }
//                });
//    }

    public void onResponseCoinMarketCap(List<Token> tokens) {

        String ethKrw = String.format("%,.2f", tokens.get(0).getAmountInKrw());
        //String bnbKrw = String.format("%,.2f", tokens.get(1).getAmountInKrw());
        String ttapKrw = String.format("%,.2f", tokens.get(1).getAmountInKrw());
        
        binding.ethKrw.setText(ethKrw + " KRW");
       // binding.bnbKrw.setText(bnbKrw + " KRW");
        binding.ttapKrw.setText(ttapKrw + " KRW");

    }

}
