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

//        if (tokenName.equals("ETH")) {
//            binding.logo.setImageDrawable(getDrawable(R.drawable.eth_icon));
//            binding.cymbol.setText("ETH");
//        } else if (tokenName.equals("BNB")) {
//            binding.logo.setImageDrawable(getDrawable(R.drawable.bnb_icon));
//            binding.cymbol.setText("BNB");
//        }



        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.eth_icon));
            binding.cymbol.setText("ETH");
        } else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bnb_icon));
            binding.cymbol.setText("BNB");
        } else if (tokenName.equals("USDT")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdt));
            binding.cymbol.setText("USDT");
        } else if (tokenName.equals("TTAP")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ttap_new_icon));
            binding.cymbol.setText("TTAP");
        } else if (tokenName.equals("DAI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.dai));
            binding.cymbol.setText("DAI");
        } else if (tokenName.equals("LINK")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.link));
            binding.cymbol.setText("LINK");
        } else if (tokenName.equals("UNI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.uni));
            binding.cymbol.setText("UNI");
        } else if (tokenName.equals("USDC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdc));
            binding.cymbol.setText("USDC");
        } else if (tokenName.equals("WBTC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wbtc));
            binding.cymbol.setText("WBTC");
        } else if (tokenName.equals("VEN")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ven));
            binding.cymbol.setText("VEN");
        } else if (tokenName.equals("THETA")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.theta));
            binding.cymbol.setText("THETA");
        } else if (tokenName.equals("WFIL")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wfil));
            binding.cymbol.setText("WFIL");
        } else if (tokenName.equals("BUSD")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.busd));
            binding.cymbol.setText("BUSD");
        } else if (tokenName.equals("OKB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.okb));
            binding.cymbol.setText("OKB");
        } else if (tokenName.equals("CRO")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cro));
            binding.cymbol.setText("CRO");
        } else if (tokenName.equals("cUSDC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cusdc));
            binding.cymbol.setText("cUSDC");
        }



        binding.fee.setText(fee);
        binding.amount.setText(amount);
        binding.toAddress.setText(receiver);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.fromAddress.setText(preferencesUtil.getWalletAddress());
       // binding.endImage.setImageDrawable(getDrawable(R.drawable.qr_code_icon));
        binding.endImage.setImageDrawable(getResources().getDrawable(R.drawable.qr_code_icon));

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
