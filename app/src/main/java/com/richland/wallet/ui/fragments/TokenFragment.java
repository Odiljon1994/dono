package com.richland.wallet.ui.fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.adapter.TransactionsAdapter;
import com.richland.wallet.api.ApiUtils;
import com.richland.wallet.databinding.FragmentTokenBinding;
import com.richland.wallet.databinding.FragmentWalletBinding;
import com.richland.wallet.di.ViewModelFactory;
import com.richland.wallet.models.Transaction;
import com.richland.wallet.ui.ReceiveActivity;
import com.richland.wallet.ui.SendActivity;
import com.richland.wallet.ui.viewmodel.EthereumVM;
import com.richland.wallet.util.PreferencesUtil;
import com.richland.wallet.web3.EthManager;
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
    String tokenName = "";
    String contractAddress = "";
    String amountInKrw = "";

    private EthereumVM ethereumVM;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_token, container, false);
        View view = binding.getRoot();

        tokenName = getArguments().getString("tokenName");
        amountInKrw = getArguments().getString("KRW");
        contractAddress = getArguments().getString("contractAddress");
        binding.tokenName.setText(tokenName);
        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.eth_icon));
        } else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bnb_icon));
        }

        binding.amountInKrw.setText(amountInKrw + " KRW");


        ethereumVM = ViewModelProviders.of(this, viewModelFactory).get(EthereumVM.class);
        ethereumVM.transactions().observe(getActivity(), this::items);
        binding.swipeRefreshLayout.setRefreshing(true);

        walletAddress = preferencesUtil.getWalletAddress();

        binding.receive.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ReceiveActivity.class));
        });
        binding.send.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SendActivity.class);
            intent.putExtra("tokenName", tokenName);
            intent.putExtra("contractAddress", contractAddress);
            startActivity(intent);
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
                        adapter.setItems(allTransactionList);
                        break;
                    case 1: // SENT
                        adapter.setItems(sendTransactionList);
                        break;
                    case 2: // RECEIVED
                        adapter.setItems(receiveTransactionList);
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

        adapter = new TransactionsAdapter(walletAddress, getActivity(), new TransactionsAdapter.EmptyViewListener() {
            @Override
            public void showItems(boolean isEmpty) {
                binding.recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onClick(Transaction transaction) {

            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
        loadData();
        binding.swipeRefreshLayout.setOnRefreshListener(this::loadData);



        return view;

    }

    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();

        ethManager.init(ApiUtils.getInfura());

        if (contractAddress.toLowerCase().equals(preferencesUtil.getWalletAddress().toLowerCase())) {
            ethManager.balanceInEth(preferencesUtil.getWalletAddress(), getActivity())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        binding.tokenAmount.setText(balance.toString());
                    }, error -> {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        } else {
            ethManager.getTokenBalance(walletAddress, "", contractAddress, getActivity())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        binding.tokenAmount.setText(balance.toString());
                    }, error -> {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        }

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
        receiveTransactionList = new ArrayList<>();
        sendTransactionList = new ArrayList<>();



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