package com.mcs.wallet.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ActivityWalletImportTypeBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mcs.wallet.util.PreferencesUtil;

import java.util.Locale;

import javax.inject.Inject;

public class ImportWalletTypeActivity extends AppCompatActivity {

    private ActivityWalletImportTypeBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_import_type);

        setAppLocale(preferencesUtil.getLANGUAGE());
        binding.seeds.setOnClickListener(v -> {
            setAppLocale(preferencesUtil.getLANGUAGE());
            startActivity(new Intent(ImportWalletTypeActivity.this, ImportWalletActivity.class));
        });

        binding.qrCode.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {

            setAppLocale(preferencesUtil.getLANGUAGE());
            Intent intent = new Intent(ImportWalletTypeActivity.this, ImportWalletActivity.class);
            intent.putExtra("seeds", scanResult.getContents());
            startActivity(intent);

        }
    }
}
