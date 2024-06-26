package com.mcs.wallet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ActivityWalletImportedBinding;
import com.mcs.wallet.util.PreferencesUtil;

import javax.inject.Inject;


public class WalletImportedActivity extends AppCompatActivity {
    private ActivityWalletImportedBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_imported);

        binding.nextBtn.setOnClickListener(v -> {
            preferencesUtil.saveIsRegistered(true);
            Intent intent = new Intent(WalletImportedActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


    }
}
