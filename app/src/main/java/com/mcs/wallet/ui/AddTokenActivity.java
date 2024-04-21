package com.mcs.wallet.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.R;
import com.mcs.wallet.adapter.ExistingTokensAdapter;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.database.DatabaseMainnetToken;
import com.mcs.wallet.databinding.ActivityAddTokenBinding;
import com.mcs.wallet.models.ExistingTokenModel;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class AddTokenActivity extends AppCompatActivity {

    private ActivityAddTokenBinding binding;
    ExistingTokensAdapter adapter;
    String ethAddress;
    private String smartContractAddress = "";
    private String tokenDecimals = "";
    private String tokenSymbol = "";
    private String tokenName = "";
    private boolean isFirstTabSelected = true;
    DatabaseMainnetToken databaseMainnetToken;
    ProgressDialog progressDialog;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_token);

        ethAddress = preferencesUtil.getWalletAddress();
        databaseMainnetToken = new DatabaseMainnetToken(this);
        initClickListeners();
        List<ExistingTokenModel> items = new ArrayList<>();


        // Test net
//        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdt), "Tether USD (USDT)", "USDT", "0x516de3a7a567d81737e3a46ec4ff9cfd1fcb0136"));
//        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.dai), "Dai Stablecoin (DAI)", "DAI", "0xc2118d4d90b274016cb7a54c03ef52e6c537d957"));
//        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.comp), "Compound", "COMP", "0x1fe16de955718cfab7a44605458ab023838c2793"));
//        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.uni), "Uniswap (UNI)", "UNI", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984"));
//        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.ceth), "Compound Ether (cETH)", "cETH", "0xbe839b6d93e3ea47effcca1f27841c917a8794f3"));
//        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdc), "USD Coin (USDC)", "USDC", "0x0d9c8723b343a8368bebe0b5e89273ff8d712e3c"));

        // Main net
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdt), "Tether USD (USDT)", "USDT", "0xdac17f958d2ee523a2206206994597c13d831ec7"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.dai), "Dai Stablecoin (DAI)", "DAI", "0x6b175474e89094c44da98b954eedeac495271d0f"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.link), "ChainLink Token (LINK)", "LINK", "0x514910771af9ca656af840dff83e8264ecf986ca"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.uni), "Uniswap (UNI)", "UNI", "0x1f9840a85d5af5bf1d1762f925bdaddc4201f984"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdc), "USD Coin (USDC)", "USDC", "0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.wbtc), "Wrapped BTC (WBTC)", "WBTC", "0x2260fac5e5542a773aa44fbcfedf7c193bc2c599"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.ven), "VeChain (VEN)", "VEN", "0xd850942ef8811f2a866692a623011bde52a462c1"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.theta), "Theta Token (THETA)", "THETA", "0x3883f5e181fccaf8410fa61e12b59bad963fb645"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.wfil), "Wrapped Filecoin (WFIL)", "WFIL", "0x6e1A19F235bE7ED8E3369eF73b196C07257494DE"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.busd), "Binance USD (BUSD)", "BUSD", "0x4fabb145d64652a948d72533023f6e7a623c7c53"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.okb), "OKB (OKB)", "OKB", "0x75231f58b43240c9718dd58b4967c5114342a86c"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.cro), "Crypto.com Coin (CRO)", "CRO", "0xa0b73e1ff0b80914ab6fe0444e65848c4c34450b"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.cusdc), "Compound USD Coin (cUSDC)", "cUSDC", "0x39aa39c021dfbae8fac545936693ac917d5e7563"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.ht), "HuobiToken (HT)", "HT", "0x6f259637dcd74c767781e37bc6133cd6a68aa161"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.ceth), "Compound Ether (cETH)", "cETH", "0x4ddc2d193948926d02f9b1fe9e1daa0718270ed5"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.mkr), "Maker (MKR)", "MKR", "0x9f8f72aa9304c8b593d555f12ef6589cc3a579a2"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.cdai), "Compound Dai (cDAI)", "cDAI", "0x5d3a536E4D6DbD6114cc1Ead35777bAB948E3643"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.comp), "Compound (COMP)", "COMP", "0xc00e94cb662c3520282e6f5717214004a7f26888"));
        items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.chz), "ChiliZ (CHZ)", "CHZ", "0x3506424f91fd33084466f402d5d97f05f8e3b4af"));

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ExistingTokensAdapter(items, this, item -> {
            smartContractAddress = item.getContractAddress();
            tokenSymbol = item.getTokenSymbol();
            tokenName = item.getTokenName();
            binding.existingTokenTab.setClickable(false);
            binding.customTokenTab.setClickable(false);
            binding.customTokensLayout.setVisibility(View.GONE);
            binding.existingTokensLayout.setVisibility(View.GONE);
            binding.newToken.setVisibility(View.VISIBLE);
            binding.tokenName.setText(item.getTokenName());
            binding.logo.setImageDrawable(item.getLogo());
            binding.signal.setText(R.string.would_you_like_add_token);
            binding.signal.setVisibility(View.VISIBLE);
            binding.next.setText(R.string.add);
            binding.next.setVisibility(View.VISIBLE);
        });

        binding.recyclerview.setAdapter(adapter);



        binding.next.setOnClickListener(v -> {
            if (!binding.next.getText().toString().equals("추가")) {
           // if (!binding.next.getText().toString().equals(R.string.add)) {

                if (isFirstTabSelected) {

                } else {
                    if (!smartContractAddress.equals("") && !tokenDecimals.equals("") && !tokenSymbol.equals("")) {
                        binding.existingTokenTab.setClickable(false);
                        binding.customTokenTab.setClickable(false);
                        binding.customTokensLayout.setVisibility(View.GONE);
                        binding.newToken.setVisibility(View.VISIBLE);
                        binding.tokenName.setText(tokenName);
                        binding.signal.setText(R.string.would_you_like_add_token);
                        binding.signal.setVisibility(View.VISIBLE);
                        binding.next.setText(R.string.add);

                        if (tokenSymbol.equals("USDT")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdt));
                        } else if (tokenSymbol.equals("DAI")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.dai));
                        } else if (tokenSymbol.equals("LINK")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.link));
                        } else if (tokenSymbol.equals("UNI")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.uni));
                        } else if (tokenSymbol.equals("USDC")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdc));
                        } else if (tokenSymbol.equals("WBTC")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wbtc));
                        } else if (tokenSymbol.equals("VEN")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ven));
                        } else if (tokenSymbol.equals("THETA")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.theta));
                        } else if (tokenSymbol.equals("WFIL")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wfil));
                        } else if (tokenSymbol.equals("BUSD")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.busd));
                        } else if (tokenSymbol.equals("OKB")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.okb));
                        } else if (tokenSymbol.equals("CRO")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cro));
                        } else if (tokenSymbol.equals("cUSDC")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cusdc));
                        } else if (tokenSymbol.equals("HT")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ht));
                        } else if (tokenSymbol.equals("cETH")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ceth));
                        } else if (tokenSymbol.equals("MKR")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.mkr));
                        } else if (tokenSymbol.equals("cDAI")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cdai));
                        } else if (tokenSymbol.equals("COMP")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.comp));
                        } else if (tokenSymbol.equals("CHZ")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.chz));
                        } else if (tokenSymbol.equals("XRP")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.xrp));
                        } else if (tokenSymbol.equals("DOGE")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.doge));
                        } else if (tokenSymbol.equals("ADA")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ada));
                        } else if (tokenSymbol.equals("BCH")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bch));
                        } else if (tokenSymbol.equals("LTC")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ltc));
                        } else if (tokenSymbol.equals("EOS")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.eos));
                        } else if (tokenSymbol.equals("CAKE")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cake));
                        } else if (tokenSymbol.equals("BUSD-T")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.busdt));
                        } else if (tokenSymbol.equals("YFI")) {
                            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.yfi));
                        }
                    } else {

                    }
                }
            } else {

                addData();
            }
        });
    }

    public void initClickListeners() {
        binding.toolbar.title.setText(R.string.add_token);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.existingTokenTab.setOnClickListener(v -> {
            binding.next.setVisibility(View.GONE);
            isFirstTabSelected = true;
            binding.customTokensLayout.setVisibility(View.GONE);
            binding.existingTokensLayout.setVisibility(View.VISIBLE);
            binding.existingTokenTab.setTextColor(Color.parseColor("#4258f9"));
            binding.customTokenTab.setTextColor(Color.parseColor("#929292"));
        });
        binding.customTokenTab.setOnClickListener(v -> {
            binding.next.setVisibility(View.VISIBLE);
            isFirstTabSelected = false;
            binding.existingTokensLayout.setVisibility(View.GONE);
            binding.customTokensLayout.setVisibility(View.VISIBLE);
            binding.existingTokenTab.setTextColor(Color.parseColor("#929292"));
            binding.customTokenTab.setTextColor(Color.parseColor("#4258f9"));
        });

        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });

        binding.smartContract.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 42) {
                    addTokenByContract(binding.smartContract.getText().toString().trim(), ethAddress, "");

                    progressDialog = ProgressDialog.show(AddTokenActivity.this, "", getResources().getString(R.string.loading_data), true);
                }
            }
        });

    }

    public void addTokenByContract(String contractAddress,
                                   String walletAddress,
                                   String password) {
        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());


        ethManager.searchTokenByContractAddress(contractAddress, walletAddress, password, this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(token -> {
                    progressDialog.dismiss();
                    binding.error.setText("");
                    binding.decimals.setText(token.getDecimals());
                    binding.tokenSymbol.setText(token.getSymbol());
                    smartContractAddress = contractAddress;
                    tokenDecimals = token.getDecimals();
                    tokenSymbol = token.getSymbol();
                    tokenName = token.getTokenName();
                }, error -> {
                    progressDialog.dismiss();
                    binding.error.setText(R.string.check_token_net);
                    System.out.println("ETH Error ** ** ** " + error.getMessage());
                });

    }

    public void addData() {
        boolean isTokenExist = false;
        Cursor data;

        data = databaseMainnetToken.getData();


        while (data.moveToNext()) {

            String contractAddress = data.getString(3);

            if (contractAddress.equals(smartContractAddress)) {
                isTokenExist = true;
            }
        }
        if (isTokenExist) {
            Toast.makeText(this, R.string.already_added_token, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddTokenActivity.this, MainActivity.class));

        } else {
            boolean insetData = databaseMainnetToken.addData(tokenSymbol, tokenName, smartContractAddress);
            if (insetData) {
                startActivity(new Intent(AddTokenActivity.this, MainActivity.class));

            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


