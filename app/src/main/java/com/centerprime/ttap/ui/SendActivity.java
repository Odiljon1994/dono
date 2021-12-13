package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.databinding.ActivitySendBinding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.AddressesBookModel;
import com.centerprime.ttap.models.CurrentFeeModel;
import com.centerprime.ttap.ui.dialogs.AddressesBookDialog;
import com.centerprime.ttap.ui.dialogs.DirectQuestionDialog;
import com.centerprime.ttap.ui.viewmodel.CurrentFeeVM;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jnr.ffi.annotations.In;

public class SendActivity extends AppCompatActivity {
    ActivitySendBinding binding;
    private CurrentFeeVM currentFeeVM;
    private String walletAddress;
    @Inject
    ViewModelFactory viewModelFactory;
    private double balance = 0;
    private double fee = 0;

    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        currentFeeVM = ViewModelProviders.of(this, viewModelFactory).get(CurrentFeeVM.class);
        currentFeeVM.item().observe(this, this::currentFee);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.endImage.setImageDrawable(getDrawable(R.drawable.share_icon));

        currentFeeVM.getCurrentFee();
        walletAddress = preferencesUtil.getWalletAddress();
        checkBalance();

        binding.nextBtn.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.receiverAddress.getText().toString())
                    && !TextUtils.isEmpty(binding.amount.getText().toString())) {

                if (balance >= fee) {
                    Intent intent = new Intent(SendActivity.this, VerifySendingActivity.class);
                    intent.putExtra("receiverAddress", binding.receiverAddress.getText().toString());
                    intent.putExtra("fee", String.valueOf(fee));
                    intent.putExtra("amount", binding.amount.getText().toString());
                    startActivity(intent);
                } else {
                    binding.errorMessage.setText("수수료 부족합니다");
                }


            } else if (TextUtils.isEmpty(binding.receiverAddress.getText().toString())) {
                binding.errorMessage.setText("Please enter receiver address");
            } else if (TextUtils.isEmpty(binding.amount.getText().toString())) {
                binding.errorMessage.setText("Please enter token amount");
            }
        });

        binding.addressBook.setOnClickListener(v -> {
            showDialog();
        });

    }

    public void currentFee(CurrentFeeModel currentFeeModel) {
        binding.currentFee.setText(String.valueOf(currentFeeModel.getFee()));
        fee = currentFeeModel.getFee();
    }

    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.getTokenBalance(walletAddress, "", ApiUtils.getContractAddress(), this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    balance = Double.parseDouble(response.toString());
                }, error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

}
