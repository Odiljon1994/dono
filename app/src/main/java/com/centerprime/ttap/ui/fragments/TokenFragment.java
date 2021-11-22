package com.centerprime.ttap.ui.fragments;

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
import androidx.lifecycle.ViewModelProviders;

import com.centerprime.ethereum_client_sdk.EthManager;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.TransactionsAdapter;
import com.centerprime.ttap.databinding.FragmentTokenBinding;
import com.centerprime.ttap.databinding.FragmentWalletBinding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.Transaction;
import com.centerprime.ttap.ui.ReceiveActivity;
import com.centerprime.ttap.ui.SendActivity;
import com.centerprime.ttap.ui.viewmodel.EthereumVM;
import com.centerprime.ttap.util.PreferencesUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TokenFragment extends Fragment {
    FragmentTokenBinding binding;
    TabLayout tabLayout;
    List<Transaction> allTransactionList;
    List<Transaction> receiveTransactionList;
    List<Transaction> sendTransactionList;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private TransactionsAdapter adapter;
    private String walletAddress;

    private EthereumVM ethereumVM;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_token, container, false);
        View view = binding.getRoot();
        ethereumVM = ViewModelProviders.of(getActivity(), viewModelFactory).get(EthereumVM.class);
        ethereumVM.transactions().observe(getActivity(), this::items);
        binding.swipeRefreshLayout.setRefreshing(true);

        walletAddress = "0x" + preferencesUtil.getWalletAddress();

        binding.receive.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ReceiveActivity.class));
        });
        binding.send.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SendActivity.class));
        });

        tabLayout = binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText("전체"));
        tabLayout.addTab(tabLayout.newTab().setText("입금"));
        tabLayout.addTab(tabLayout.newTab().setText("출금"));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tabLayout.getSelectedTabPosition()) {
                    case 0: // ALL

                        break;
                    case 1: // SENT

                        break;
                    case 2: // RECEIVED

                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;

    }

    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();
        //ethManager.init("https://mainnet.infura.io/v3/a396c3461ac048a59f389c7778f06689"); // mainnet infura
        ethManager.init("https://ropsten.infura.io/v3/a396c3461ac048a59f389c7778f06689"); // infura for ropsten testnet


        ethManager.balanceInEth(walletAddress, getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {



                }, error -> {
                    /**
                     * if function fails error can be caught in this block
                     */
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                });
    }

    public void loadData() {
        adapter.clearItems();
        checkBalance();
        allTransactionList = new ArrayList<>();
        receiveTransactionList = new ArrayList<>();
        sendTransactionList = new ArrayList<>();
        ethereumVM.getTransactions(walletAddress);
        binding.swipeRefreshLayout.setRefreshing(false);

        if (binding.tabLayout.getSelectedTabPosition() != 0) {

            binding.tabLayout.getTabAt(0).select();
        }
    }

    public void items(List<Transaction> items) {

        allTransactionList = items;

        adapter.setItems(allTransactionList);
        binding.swipeRefreshLayout.setRefreshing(false);

        for (int i = 0; i < items.size(); i++) {

            if (items.get(i).getFrom().toLowerCase().equals(preferencesUtil.getWalletAddress())) {
                sendTransactionList.add(items.get(i));
            } else {
                receiveTransactionList.add(items.get(i));
            }

        }

        if (binding.tabLayout.getSelectedTabPosition() != 0) {

            binding.tabLayout.getTabAt(0).select();
        }
    }
}
