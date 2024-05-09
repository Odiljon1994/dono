package com.mcs.wallet.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.R;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.databinding.ActivitySendErc1155Binding;
import com.mcs.wallet.databinding.ActivitySendVerifyErc1155Binding;
import com.mcs.wallet.di.ViewModelFactory;
import com.mcs.wallet.models.AddressesBookModel;
import com.mcs.wallet.ui.dialogs.AddressesBookDialog;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendErc1155VerifyActivity extends AppCompatActivity {

    private ActivitySendVerifyErc1155Binding binding;
    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    PreferencesUtil preferencesUtil;

    private String contractAddress;
    private int tokenId;

    private String receiverAddress;

    private int tokenAmount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_verify_erc1155);
        binding.backBtn.setOnClickListener(v -> finish());
        contractAddress = getIntent().getStringExtra("contract_address");
        tokenId = getIntent().getIntExtra("token_id", -1);
        receiverAddress = getIntent().getStringExtra("receiver_address");
        tokenAmount = getIntent().getIntExtra("token_amount", -1);


        String tokenType = getType(contractAddress);
        if (tokenType.equals("GOLD")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.gold_card));
            binding.cymbol.setText("GOLD");
            binding.cymbolFee.setText("GOLD");
        } else if (tokenType.equals("SILVER")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.silver_card));
            binding.cymbol.setText("SILVER");
            binding.cymbolFee.setText("SILVER");
        } else if (tokenType.equals("BRONZE")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bronze_card));
            binding.cymbol.setText("BRONZE");
            binding.cymbolFee.setText("BRONZE");
        }

        String walletAddress = preferencesUtil.getWalletAddress();
        binding.amount.setText("" + tokenAmount);
        binding.fromAddress.setText(walletAddress);
        binding.toAddress.setText(receiverAddress);

        binding.goBack.setOnClickListener((v) -> {
            finish();
        });

        binding.confirm.setOnClickListener((v) -> {
            EthManager.getInstance().createSignature(walletAddress, "", this,
                            receiverAddress, tokenId, tokenAmount, contractAddress)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> EthManager.getInstance()
                            .sendERC1155TokenBySignature(response)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(res -> {
                                Intent intent;
                                if (res != null) {
                                    intent = new Intent(SendErc1155VerifyActivity.this, SentResultActivity.class);
                                    intent.putExtra("contract_address", contractAddress);
                                    intent.putExtra("token_id", tokenId);
                                    intent.putExtra("token_amount", tokenAmount);
                                    intent.putExtra("receiver_address", receiverAddress);
                                    intent.putExtra("tx_hash", res);
                                } else {
                                    intent = new Intent(SendErc1155VerifyActivity.this, SentFailureActivity.class);
                                    intent.putExtra("contract_address", contractAddress);
                                    intent.putExtra("token_id", tokenId);
                                    intent.putExtra("token_amount", tokenAmount);
                                    intent.putExtra("receiver_address", receiverAddress);
                                }
                                startActivity(intent);
                                finish();
                            }, error -> {
                                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println(error.getMessage());
                            }), error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
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
