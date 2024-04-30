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


        if (!ApiUtils.isMainnet) {
            // Test net
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdt), "Tether USD (USDT)", "USDT", "0xbCF39d8616d15FD146dd5dB4a86b4f244A9Bc772"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.dai), "Dai Stablecoin (DAI)", "DAI", "0x542a0f0F599228A9A92932aAF110a2b69DbD2C11"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.uni), "Uniswap (UNI)", "UNI", "0x39179cb99f7BA1244665AA7Ff2c2886CBCBcE3bc"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdc), "USD Coin (USDC)", "USDC", "0x6EEBe75caf9c579B3FBA9030760B84050283b50a"));
        } else {
            // Main net
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdt), "Tether USD (USDT)", "USDT", "0xc2132D05D31c914a87C6611C10748AEb04B58e8F"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.dai), "Dai Stablecoin (DAI)", "DAI", "0x8f3Cf7ad23Cd3CaDbD9735AFf958023239c6A063"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.link), "ChainLink Token (LINK)", "LINK", "0xb0897686c545045aFc77CF20eC7A532E3120E0F1"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.uni), "Uniswap (UNI)", "UNI", "0xb33EaAd8d922B1083446DC23f610c2567fB5180f"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.usdc), "USD Coin (USDC)", "USDC", "0x3c499c542cEF5E3811e1192ce70d8cC03d5c3359"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.wbtc), "Wrapped BTC (WBTC)", "WBTC", "0x1BFD67037B42Cf73acF2047067bd4F2C47D9BfD6"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.theta), "Theta Token (THETA)", "THETA", "0xB46E0ae620EFd98516f49bb00263317096C114b2"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.busd), "Binance USD (BUSD)", "BUSD", "0xdAb529f40E671A1D4bF91361c21bf9f0C9712ab7"));
            items.add(new ExistingTokenModel(ContextCompat.getDrawable(this, R.drawable.chz), "ChiliZ (CHZ)", "CHZ", "0xf1938Ce12400f9a761084E7A80d37e732a4dA056"));
        }

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


