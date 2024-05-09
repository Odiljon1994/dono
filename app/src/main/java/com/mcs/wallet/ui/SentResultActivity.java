package com.mcs.wallet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.R;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.databinding.ActivitySentResultBinding;

public class SentResultActivity extends AppCompatActivity {

    private ActivitySentResultBinding binding;

    private String contractAddress;
    private int tokenId;
    private String receiverAddress;
    private int tokenAmount;

    private String txHash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sent_result);

        contractAddress = getIntent().getStringExtra("contract_address");
        tokenId = getIntent().getIntExtra("token_id", -1);
        receiverAddress = getIntent().getStringExtra("receiver_address");
        txHash = getIntent().getStringExtra("tx_hash");
        tokenAmount = getIntent().getIntExtra("token_amount", -1);


        String tokenType = getType(contractAddress);
        if (tokenType.equals("GOLD")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.gold_card));
            binding.cymbol.setText("GOLD");
        } else if (tokenType.equals("SILVER")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.silver_card));
            binding.cymbol.setText("SILVER");
        } else if (tokenType.equals("BRONZE")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bronze_card));
            binding.cymbol.setText("BRONZE");
        }

        binding.amount.setText("" + tokenAmount);
        binding.txHash.setText(txHash);


        binding.toolbar.backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        binding.okBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private String getType(String contractAddress) {
        if (contractAddress.equals(ApiUtils.getGoldERC1155ContractAddress())) {
            return "GOLD";
        } else if (contractAddress.equals(ApiUtils.getSilverERC1155ContractAddress())) {
            return "SILVER";
        } else if (contractAddress.equals(ApiUtils.getBronzeERC1155ContractAddress())) {
            return "BRONZE";
        }
        return "";
    }
}
