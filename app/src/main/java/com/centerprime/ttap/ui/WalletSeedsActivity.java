package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ethereum_client_sdk.EthManager;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityWalletSeedsBinding;
import com.centerprime.ttap.util.PreferencesUtil;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jnr.ffi.annotations.In;

public class WalletSeedsActivity extends AppCompatActivity {
    private ActivityWalletSeedsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_seeds);

        binding.backBtn.setOnClickListener(v -> finish());

        EthManager ethManager = EthManager.getInstance();
        ethManager.init("https://mainnet.infura.io/v3/7c36e7f5656d4384bbcb2cbaf67ad699");


        ethManager.exportKeyStore(preferencesUtil.getWalletAddress(), WalletSeedsActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletAddress -> {
                    binding.seeds.setText(walletAddress);

                });

        binding.okBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletSeedsActivity.this, SaveQrCodeActivity.class);
            intent.putExtra("seeds", binding.seeds.getText().toString());
            startActivity(intent);
        });
    }
}
