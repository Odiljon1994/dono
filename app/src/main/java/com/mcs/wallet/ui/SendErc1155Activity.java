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
import com.mcs.wallet.di.ViewModelFactory;
import com.mcs.wallet.models.AddressesBookModel;
import com.mcs.wallet.ui.dialogs.AddressesBookDialog;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;

import org.web3j.crypto.WalletUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendErc1155Activity extends AppCompatActivity {

    private ActivitySendErc1155Binding binding;
    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    PreferencesUtil preferencesUtil;

    private String contractAddress;
    private int tokenId;

    private int tokenBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send_erc1155);
        binding.backBtn.setOnClickListener(v -> finish());
        contractAddress = getIntent().getStringExtra("contract_address");
        tokenId = getIntent().getIntExtra("token_id", -1);

        String tokenType = getType(contractAddress);
        if (tokenType.equals("GOLD")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.gold_card));
            binding.cardName.setText("GOLD");
        } else if (tokenType.equals("SILVER")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.silver_card));
            binding.cardName.setText("SILVER");
        } else if (tokenType.equals("BRONZE")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bronze_card));
            binding.cardName.setText("BRONZE");
        }

        binding.addressBook.setOnClickListener(v -> {
            showDialog();
        });

        binding.max.setOnClickListener(view -> {
            binding.amount.setText("" + tokenBalance);
        });

        binding.nextBtn.setOnClickListener(v -> {

            String amount = binding.amount.getText().toString();
            if (amount.equals("") || amount.equals("0")) {
                Toast.makeText(this, "Please enter Amount", Toast.LENGTH_SHORT).show();
                return;
            }

            int amountInInt = Integer.parseInt(amount);
            if (tokenBalance < amountInInt) {
                Toast.makeText(this, "Not Enough Token", Toast.LENGTH_SHORT).show();
                return;
            }

            String receiverAddress = binding.receiverAddress.getText().toString();
            if (receiverAddress.isEmpty() || !WalletUtils.isValidAddress(receiverAddress)){
                Toast.makeText(this, "Please enter valid wallet address", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(SendErc1155Activity.this, SendErc1155VerifyActivity.class);
            intent.putExtra("contract_address", contractAddress);
            intent.putExtra("token_id", tokenId);
            intent.putExtra("receiver_address", receiverAddress);
            intent.putExtra("token_amount", amountInInt);
            startActivity(intent);
        });

        EthManager.getInstance().getERC1155TokenBalance(preferencesUtil.getWalletAddress(), "", contractAddress, tokenId, this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    tokenBalance = balance.intValue();
                    binding.firstItemAmount.setText(balance.toString());
                }, error -> {
                    System.out.println(error.getMessage());
                });

    }

    public void showDialog() {
        AddressesBookDialog addressesBookDialog = new AddressesBookDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(addressesBookDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        AddressesBookDialog.ClickListener clickListener = new AddressesBookDialog.ClickListener() {
            @Override
            public void onClick(AddressesBookModel model) {
                dialog.dismiss();
                binding.receiverAddress.setText(model.getWalletAddress());
            }
        };
        addressesBookDialog.setClickListener(clickListener);
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
