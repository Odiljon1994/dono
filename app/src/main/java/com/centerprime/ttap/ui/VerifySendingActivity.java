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
    private String tokenName;
    private String contractAddress;
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
        tokenName = getIntent().getStringExtra("tokenName");
        contractAddress = getIntent().getStringExtra("contractAddress");

        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getDrawable(R.drawable.eth_icon));
            binding.cymbol.setText("ETH");
        } else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getDrawable(R.drawable.bnb_icon));
            binding.cymbol.setText("BNB");
        }

        binding.fee.setText(fee);
        binding.amount.setText(amount);
        binding.toAddress.setText(receiver);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.fromAddress.setText(preferencesUtil.getWalletAddress());
        binding.endImage.setImageDrawable(getDrawable(R.drawable.qr_code_icon));

        binding.confirm.setOnClickListener(v -> {
            Intent intent = new Intent(VerifySendingActivity.this, SendOtpActivity.class);
            intent.putExtra("receiverAddress", receiver);
            intent.putExtra("amount", amount);
            intent.putExtra("fee", fee);
            intent.putExtra("tokenName", tokenName);
            intent.putExtra("contractAddress", contractAddress);
            startActivity(intent);
        });

    }
}
