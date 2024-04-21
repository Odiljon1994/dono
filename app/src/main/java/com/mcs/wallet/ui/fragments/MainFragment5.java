package com.mcs.wallet.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.adapter.SearchSectionsAdapter;
import com.mcs.wallet.di.ViewModelFactory;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;
import com.mcs.wallet.R;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.databinding.FragmentMain5Binding;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainFragment5 extends Fragment {
    FragmentMain5Binding binding;
    private String walletAddress;

    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    
    private SearchSectionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main5, container, false);

        View view = binding.getRoot();


        walletAddress = preferencesUtil.getWalletAddress();
      //  checkBalance();
        checkVonusBalance();
        binding.progressBar.getProgress();

        binding.refreshBtn.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
        //    checkBalance();
            checkVonusBalance();
        });

        return view;
    }

    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.getTokenBalance(walletAddress, "", ApiUtils.getContractAddress(), getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    binding.amount.setText(balance.toString());
                    binding.progressBar.setVisibility(View.GONE);

                }, error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    // Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());

                });
    }

    public void checkVonusBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.balanceInEth(walletAddress, getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    binding.amount.setText(balance.toString());
                    binding.progressBar.setVisibility(View.GONE);

                }, error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());

                });
    }
}
