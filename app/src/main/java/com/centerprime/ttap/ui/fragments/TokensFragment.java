package com.centerprime.ttap.ui.fragments;

import android.app.ProgressDialog;
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

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.TransactionAdapter;
import com.centerprime.ttap.adapter.TransactionsAdapter;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.databinding.FragmentTokenBinding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.Transaction;
import com.centerprime.ttap.models.TransactionsModel;
import com.centerprime.ttap.ui.MainActivity;
import com.centerprime.ttap.ui.ReceiveActivity;
import com.centerprime.ttap.ui.SendActivity;
import com.centerprime.ttap.ui.TransactionDetailsActivity;
import com.centerprime.ttap.ui.viewmodel.EtherScanVM;
import com.centerprime.ttap.ui.viewmodel.EthereumVM;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TokensFragment extends Fragment {
    FragmentTokenBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private TransactionAdapter adapter;
    private String walletAddress;
    String tokenName = "";
    String contractAddress = "";
    String amountInKrw = "";
    EtherScanVM etherScanVM;
    TabLayout tabLayout;
    List<TransactionsModel> allTransactionList;
    List<TransactionsModel> receiveTransactionList;
    List<TransactionsModel> sendTransactionList;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_token, container, false);
        View view = binding.getRoot();
        etherScanVM = ViewModelProviders.of(this, viewModelFactory).get(EtherScanVM.class);
        etherScanVM.transactions().observe(getActivity(), this::items);
        binding.swipeRefreshLayout.setRefreshing(true);


        tokenName = getArguments().getString("tokenName");
        amountInKrw = getArguments().getString("KRW");
        contractAddress = getArguments().getString("contractAddress");
        walletAddress = preferencesUtil.getWalletAddress();
        binding.tokenName.setText(tokenName);
        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.eth_icon));
        } else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.bnb_icon));
        } else if (tokenName.equals("USDT")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.usdt));
        } else if (tokenName.equals("TTAP")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.ttap_new_icon));
        } else if (tokenName.equals("DAI")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.dai));
        } else if (tokenName.equals("LINK")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.link));
        } else if (tokenName.equals("UNI")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.uni));
        } else if (tokenName.equals("USDC")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.usdc));
        } else if (tokenName.equals("WBTC")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.wbtc));
        } else if (tokenName.equals("VEN")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.ven));
        } else if (tokenName.equals("THETA")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.theta));
        } else if (tokenName.equals("WFIL")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.wfil));
        } else if (tokenName.equals("BUSD")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.busd));
        } else if (tokenName.equals("OKB")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.okb));
        } else if (tokenName.equals("CRO")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.cro));
        } else if (tokenName.equals("cUSDC")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.cusdc));
        } else if (tokenName.equals("HT")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.ht));
        } else if (tokenName.equals("cETH")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.ceth));
        } else if (tokenName.equals("MKR")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.mkr));
        } else if (tokenName.equals("cDAI")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.cdai));
        } else if (tokenName.equals("COMP")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.comp));
        } else if (tokenName.equals("CHZ")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.chz));
        } else if (tokenName.equals("ADA")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.ada));
        } else if (tokenName.equals("DOGE")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.doge));
        } else if (tokenName.equals("XRP")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.xrp));
        } else if (tokenName.equals("BCH")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.bch));
        } else if (tokenName.equals("LTC")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.ltc));
        } else if (tokenName.equals("EOS")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.eos));
        } else if (tokenName.equals("CAKE")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.cake));
        } else if (tokenName.equals("BUSD-T")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.busdt));
        } else if (tokenName.equals("YFI")) {
            binding.logo.setImageDrawable(getActivity().getDrawable(R.drawable.yfi));
        }

        binding.amountInKrw.setText(amountInKrw + " KRW");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new TransactionAdapter(preferencesUtil.getWalletAddress(), getActivity(), new TransactionAdapter.ClickListener() {
            @Override
            public void onClick(TransactionsModel item) {
                Intent intent = new Intent(getActivity(), TransactionDetailsActivity.class);


                intent.putExtra("timeStamp", item.getTimeStamp());
                intent.putExtra("hash", item.getHash());
                intent.putExtra("value", item.getValue());
                intent.putExtra("from", item.getFrom());
                intent.putExtra("to", item.getTo());
                intent.putExtra("blockNumber", item.getBlockNumber());
                intent.putExtra("blockHash", item.getBlockHash());
                intent.putExtra("isError", item.getIsError());
                startActivity(intent);
            }

            @Override
            public void showItems(boolean isEmpty) {
                binding.recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
                binding.emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            }
        });
        binding.recyclerView.setAdapter(adapter);
        loadData();
        binding.swipeRefreshLayout.setOnRefreshListener(this::loadData);

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
                    case 1: // RECEIVED
                        adapter.setItems(receiveTransactionList);
                        break;
                    case 2: // SEND
                        adapter.setItems(sendTransactionList);
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

    /**
     * refreshing transactions (SwipeRefreshLayout)
     */
    public void loadData() {
        adapter.clearItems();
        checkBalance();

        allTransactionList = new ArrayList<>();

      //  progressDialog = ProgressDialog.show(getActivity(), "", "데이터 불러오는 중…", true);
        etherScanVM.getTransactions(preferencesUtil.getWalletAddress());

        binding.swipeRefreshLayout.setRefreshing(false);

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
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                    }, error -> {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        } else {
            ethManager.getTokenBalance(walletAddress, "", contractAddress, getActivity())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        binding.tokenAmount.setText(balance.toString());
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                    }, error -> {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        }

    }

    public void items(List<TransactionsModel> items) {

      //  progressDialog.dismiss();
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
