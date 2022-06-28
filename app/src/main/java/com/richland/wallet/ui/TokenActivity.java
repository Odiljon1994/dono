package com.richland.wallet.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.adapter.TransactionAdapter;
import com.richland.wallet.api.ApiUtils;
import com.richland.wallet.databinding.FragmentTokenBinding;
import com.richland.wallet.di.ViewModelFactory;
import com.richland.wallet.models.TransactionsModel;
import com.richland.wallet.ui.viewmodel.EtherScanVM;
import com.richland.wallet.util.PreferencesUtil;
import com.richland.wallet.web3.EthManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TokenActivity extends AppCompatActivity {
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
    List<TransactionsModel> allTransactionList = new ArrayList<>();
    List<TransactionsModel> receiveTransactionList = new ArrayList<>();
    List<TransactionsModel> sendTransactionList = new ArrayList<>();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_token);

        etherScanVM = ViewModelProviders.of(this, viewModelFactory).get(EtherScanVM.class);
        etherScanVM.transactions().observe(this, this::items);
        binding.swipeRefreshLayout.setRefreshing(true);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());


        tokenName = getIntent().getStringExtra("tokenName");
        amountInKrw = getIntent().getStringExtra("KRW");
        contractAddress = getIntent().getStringExtra("contractAddress");

        walletAddress = preferencesUtil.getWalletAddress();
        binding.tokenName.setText(tokenName);
        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.eth_icon));
        } else if (tokenName.equals("G3S")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.g3s));
        }
        else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bnb_icon));
        } else if (tokenName.equals("USDT")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdt));
        } else if (tokenName.equals("TTAP")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ttap_new_icon));
        } else if (tokenName.equals("DAI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.dai));
        } else if (tokenName.equals("LINK")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.link));
        } else if (tokenName.equals("UNI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.uni));
        } else if (tokenName.equals("USDC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdc));
        } else if (tokenName.equals("WBTC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wbtc));
        } else if (tokenName.equals("VEN")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ven));
        } else if (tokenName.equals("THETA")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.theta));
        } else if (tokenName.equals("WFIL")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wfil));
        } else if (tokenName.equals("BUSD")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.busd));
        } else if (tokenName.equals("OKB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.okb));
        } else if (tokenName.equals("CRO")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cro));
        } else if (tokenName.equals("cUSDC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cusdc));
        } else if (tokenName.equals("HT")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ht));
        } else if (tokenName.equals("cETH")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ceth));
        } else if (tokenName.equals("MKR")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.mkr));
        } else if (tokenName.equals("cDAI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cdai));
        } else if (tokenName.equals("COMP")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.comp));
        } else if (tokenName.equals("CHZ")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.chz));
        } else if (tokenName.equals("ADA")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ada));
        } else if (tokenName.equals("DOGE")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.doge));
        } else if (tokenName.equals("XRP")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.xrp));
        } else if (tokenName.equals("BCH")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bch));
        } else if (tokenName.equals("LTC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ltc));
        } else if (tokenName.equals("EOS")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.eos));

        } else if (tokenName.equals("CAKE")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cake));
        } else if (tokenName.equals("BUSD-T")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.busdt));
        } else if (tokenName.equals("YFI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.yfi));
        }

        binding.amountInKrw.setText(amountInKrw + " KRW");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new TransactionAdapter(preferencesUtil.getWalletAddress(), this, new TransactionAdapter.ClickListener() {
            @Override
            public void onClick(TransactionsModel item) {
                Intent intent = new Intent(TokenActivity.this, TransactionDetailsActivity.class);


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
            startActivity(new Intent(this, ReceiveActivity.class));
        });
        binding.send.setOnClickListener(v -> {
            Intent intent = new Intent(this, SendActivity.class);
            intent.putExtra("tokenName", tokenName);
            intent.putExtra("contractAddress", contractAddress);
            startActivity(intent);
        });


        tabLayout = binding.tabLayout;
        tabLayout.addTab(tabLayout.newTab().setText(R.string.all));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.deposit));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.send));


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
            ethManager.balanceInEth(preferencesUtil.getWalletAddress(), this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        binding.tokenAmount.setText(balance.toString());
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                    }, error -> {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        } else {
            ethManager.getTokenBalance(walletAddress, "", contractAddress, this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(balance -> {
                        binding.tokenAmount.setText(balance.toString());
                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                    }, error -> {

                        binding.progressBar.setVisibility(View.GONE);
                        binding.tokenAmount.setVisibility(View.VISIBLE);
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
