package com.richland.wallet.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.databinding.ActivityWalletBinding;
import com.richland.wallet.util.PreferencesUtil;

import java.util.Locale;

import javax.inject.Inject;

public class WalletActivity extends AppCompatActivity {
    ActivityWalletBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);

        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();
        binding.privacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        binding.terms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        binding.terms.setOnClickListener(v -> startActivity(new Intent(this, TermsOfUseActivity.class)));
        binding.privacy.setOnClickListener(v -> startActivity(new Intent(this, PrivacyPolicyActivity.class)));

        binding.createWallet.setOnClickListener(v -> startActivity(new Intent(this, OtpActivity.class)));
        binding.importWallet.setOnClickListener(v -> startActivity(new Intent(this, ImportWalletTypeActivity.class)));

        binding.terms.setOnClickListener(v -> startActivity(new Intent(WalletActivity.this, TermsOfUseActivity.class)));
        binding.privacy.setOnClickListener(v -> startActivity(new Intent(WalletActivity.this, PrivacyPolicyActivity.class)));

        if (preferencesUtil.getLANGUAGE().equals("ja")) {
            binding.isJp.setChecked(true);

        } else {
            binding.isKor.setChecked(true);
        }

        binding.isJp.setOnClickListener(v -> {
            binding.isKor.setChecked(false);
            binding.isJp.setChecked(true);
            setAppLocale("ja");
            preferencesUtil.saveLanguage("ja");
            finish();
            startActivity(new Intent(WalletActivity.this, WalletActivity.class));
            // recreate();
        });

        binding.isKor.setOnClickListener(v -> {
            binding.isJp.setChecked(false);
            binding.isKor.setChecked(true);
            setAppLocale("en");
            preferencesUtil.saveLanguage("en");
            finish();
            startActivity(new Intent(WalletActivity.this, WalletActivity.class));
            //   recreate();
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

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("ok");
                return true;
            } else {

                System.out.println("no");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            System.out.println("ok");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                System.out.println("ok2");
                return true;
            } else {

                System.out.println("no2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            System.out.println("ok2");
            return true;
        }
    }
}
