package com.mcs.wallet.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.R;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.databinding.ActivityImportWalletBinding;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;
import com.mcs.wallet.web3.MnemonicWallet;

import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletActivity extends AppCompatActivity {
    private ActivityImportWalletBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    String seeds = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_import_wallet);
        setAppLocale(preferencesUtil.getLANGUAGE());

        binding.backBtn.setOnClickListener(v -> finish());
        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        seeds = getIntent().getStringExtra("seeds");

        binding.seeds.setText(seeds);

        binding.importBtn.setOnClickListener(v -> {
            String[] pairs = binding.seeds.getText().toString().split(" ");
            if (!TextUtils.isEmpty(binding.seeds.getText().toString()) && pairs.length >= 12) {

                MnemonicWallet mnemonicWallet = ethManager.importWalletByMnemonic(binding.seeds.getText().toString());
                System.out.println("***********");
                System.out.println(mnemonicWallet.getMnemonic());
                System.out.println(mnemonicWallet.getWalletAddress());
                System.out.println(mnemonicWallet.getPrivateKey());

                preferencesUtil.saveWalletAddress(mnemonicWallet.getWalletAddress());
                preferencesUtil.saveMnemonic(mnemonicWallet.getMnemonic());
                preferencesUtil.savePrivateKey(mnemonicWallet.getPrivateKey());
                System.out.println("***********");

                ethManager.importFromPrivateKey(mnemonicWallet.getPrivateKey(), ImportWalletActivity.this)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(walletAddress -> {
                            System.out.println(walletAddress);
                            //showDialog();
                            startActivity(new Intent(ImportWalletActivity.this, OtpImportWalletActivity.class));

                        });
            } else if (binding.seeds.getText().toString().equals("")) {
                binding.error.setText(R.string.incorrect_phrase);
            } else if (pairs.length < 12) {
                binding.error.setText(R.string.incorrect_phrase + " minimum 12 words");
            }
        });
    }

    private void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
