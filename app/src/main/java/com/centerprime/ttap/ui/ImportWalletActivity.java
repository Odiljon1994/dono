package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.databinding.ActivityImportWalletBinding;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;
import com.centerprime.ttap.web3.MnemonicWallet;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImportWalletActivity extends AppCompatActivity {
    private ActivityImportWalletBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_import_wallet);

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

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
                binding.error.setText("구문이 옳지 않습니다.");
            } else if (pairs.length < 12) {
                binding.error.setText("구문이 옳지 않습니다. minimum 12 words");
            }






        });
    }
}
