package com.cicenterprise.wellet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ActivityWalletImportTypeBinding;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ImportWalletTypeActivity extends AppCompatActivity {

    private ActivityWalletImportTypeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_import_type);

        binding.seeds.setOnClickListener(v -> {
            startActivity(new Intent(ImportWalletTypeActivity.this, ImportWalletActivity.class));
        });

        binding.qrCode.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {

            Intent intent = new Intent(ImportWalletTypeActivity.this, ImportWalletActivity.class);
            intent.putExtra("seeds", scanResult.getContents());
            startActivity(intent);

        }
    }
}
