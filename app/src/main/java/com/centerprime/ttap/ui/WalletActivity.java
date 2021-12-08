package com.centerprime.ttap.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityWalletBinding;

public class WalletActivity extends AppCompatActivity {
    ActivityWalletBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);

        binding.privacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        binding.terms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        binding.terms.setOnClickListener(v -> startActivity(new Intent(this, TermsOfUseActivity.class)));
        binding.privacy.setOnClickListener(v -> startActivity(new Intent(this, PrivacyPolicyActivity.class)));

        binding.createWallet.setOnClickListener(v -> startActivity(new Intent(this, OtpActivity.class)));

        binding.terms.setOnClickListener(v -> startActivity(new Intent(WalletActivity.this, TermsOfUseActivity.class)));
        binding.privacy.setOnClickListener(v -> startActivity(new Intent(WalletActivity.this, PrivacyPolicyActivity.class)));

    }
}
