package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.database.AddressesBookDB;
import com.centerprime.ttap.databinding.ActivityAddWalletaddressBinding;

import org.web3j.crypto.WalletUtils;

public class AddWalletAddressActivity extends AppCompatActivity {

    private ActivityAddWalletaddressBinding binding;
    private AddressesBookDB addressesBookDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_walletaddress);

        addressesBookDB = new AddressesBookDB(this);
        binding.toolbar.title.setText("주소록 등록");
        binding.toolbar.backBtn.setOnClickListener(v -> finish());

        binding.saveBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.walletAddress.getText().toString())
                    && !TextUtils.isEmpty(binding.name.getText().toString())) {

                if (binding.saveBtn.getText().toString().equals("확인")) {
                    if (WalletUtils.isValidAddress(binding.walletAddress.getText().toString())) {

                        binding.saveBtn.setText("수정 완료");
                        binding.error.setText("");

                    } else {
                        binding.error.setText("Wallet address is not valid");
                    }
                } else {
                    boolean insertData = addressesBookDB.addData(binding.name.getText().toString(), binding.walletAddress.getText().toString());

                    if (insertData) {
                        Intent intent = new Intent(AddWalletAddressActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }
            } else if (TextUtils.isEmpty(binding.name.getText().toString())) {
                binding.error.setText("Please enter your name..");
            } else if (TextUtils.isEmpty(binding.walletAddress.getText().toString())) {
                binding.error.setText("Please enter wallet address..");
            }
        });
    }


}
