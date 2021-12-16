package com.centerprime.ttap.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityWalletBinding;

import jnr.ffi.annotations.In;

public class WalletActivity extends AppCompatActivity {
    ActivityWalletBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
