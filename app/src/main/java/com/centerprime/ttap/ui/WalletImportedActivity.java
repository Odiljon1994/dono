package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityWalletImportedBinding;


public class WalletImportedActivity extends AppCompatActivity {
    private ActivityWalletImportedBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_imported);

        binding.nextBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ScanQrCodeActivity.class));
        });


    }
}
