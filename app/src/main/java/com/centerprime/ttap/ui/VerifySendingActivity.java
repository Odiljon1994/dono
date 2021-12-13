package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityVerifySendingBinding;
import com.centerprime.ttap.util.PreferencesUtil;

import javax.inject.Inject;

public class VerifySendingActivity extends AppCompatActivity {
    ActivityVerifySendingBinding binding;
    private String receiver;
    private String amount;
    @Inject
    PreferencesUtil preferencesUtil;
    private String fee = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_sending);

        receiver = getIntent().getStringExtra("receiverAddress");
        amount = getIntent().getStringExtra("amount");
        fee = getIntent().getStringExtra("fee");

        binding.amount.setText(amount);
        binding.toAddress.setText(receiver);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.fromAddress.setText("0x" + preferencesUtil.getWalletAddress());
        binding.endImage.setImageDrawable(getDrawable(R.drawable.qr_code_icon));

        binding.confirm.setOnClickListener(v -> {
            Intent intent = new Intent(VerifySendingActivity.this, SendOtpActivity.class);
            intent.putExtra("receiverAddress", receiver);
            intent.putExtra("amount", amount);
            intent.putExtra("fee", fee);
            startActivity(intent);
        });

    }
}
