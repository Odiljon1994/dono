package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
    private double totalAmount = 0;
    private double fee = 0;
    String tokenName = "";
    String contractAddress = "";

    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        currentFeeVM = ViewModelProviders.of(this, viewModelFactory).get(CurrentFeeVM.class);
        currentFeeVM.item().observe(this, this::currentFee);
        binding.backBtn.setOnClickListener(v -> finish());


        tokenName = getIntent().getStringExtra("tokenName");
        contractAddress = getIntent().getStringExtra("contractAddress");

        binding.endImage.setOnClickListener(v -> {
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setOrientationLocked(false);
            integrator.initiateScan();
        });


        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getDrawable(R.drawable.eth_icon));
            binding.cymbolHead.setText("ETH");
            binding.cymbol.setText("ETH");
        } else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getDrawable(R.drawable.bnb_icon));
            binding.cymbolHead.setText("BNB");
            binding.cymbol.setText("BNB");
        }

        binding.max.setOnClickListener(v -> {

            binding.amount.setText(String.valueOf(totalAmount));

        });

        binding.amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!binding.amount.getText().toString().equals("")) {

                    Double currentAmount = Double.parseDouble(binding.amount.getText().toString());
                    if (totalAmount >= currentAmount) {

                        binding.errorMessage.setText("");
                        binding.balance.setText("잔액 : " + String.valueOf(totalAmount - currentAmount));
                    } else {
                        binding.errorMessage.setText("잔액이 부족합니다");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


                if (binding.amount.getText().toString().equals("")) {
                    binding.balance.setText("잔액 : ");
                    binding.errorMessage.setText("");
                }
            }
        });

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
                    intent.putExtra("tokenName", tokenName);
                    intent.putExtra("contractAddress", contractAddress);
                    intent.putExtra("amount", binding.amount.getText().toString());
                    startActivity(intent);
                } else {
                    binding.errorMessage.setText("수수료 부족합니다");
                }


            } else if (TextUtils.isEmpty(binding.receiverAddress.getText().toString())) {
                binding.errorMessage.setText("주소와 금액을 먼저 입력해주세요.");
            } else if (TextUtils.isEmpty(binding.amount.getText().toString())) {
                binding.errorMessage.setText("주소와 금액을 먼저 입력해주세요.");
            }
        });

        binding.addressBook.setOnClickListener(v -> {
            showDialog();
        });

    }

    public void currentFee(CurrentFeeModel currentFeeModel) {
        binding.currentFee.setText(String.valueOf(currentFeeModel.getFee()) + " TTAP");
        fee = currentFeeModel.getFee();
    }

    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        if (tokenName.equals("ETH")) {
            ethManager.balanceInEth(preferencesUtil.getWalletAddress(), this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        binding.totalAmount.setText(response.toString());
                        totalAmount = Double.parseDouble(response.toString());
                    }, error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        } else if (tokenName.equals("BNB")) {
            ethManager.getTokenBalance(walletAddress, "", ApiUtils.getBnbContractAddress(), this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        binding.totalAmount.setText(response.toString());
                        totalAmount = Double.parseDouble(response.toString());
                    }, error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        } else if (tokenName.equals("TTAP")) {
            ethManager.getTokenBalance(walletAddress, "", ApiUtils.getContractAddress(), this)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        binding.totalAmount.setText(response.toString());
                        totalAmount = Double.parseDouble(response.toString());
                    }, error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.getMessage());
                    });
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {

            binding.receiverAddress.setText(scanResult.getContents());

        }
    }

}
