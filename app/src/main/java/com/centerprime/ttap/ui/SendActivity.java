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

import org.web3j.crypto.WalletUtils;

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

        binding.cymbolHead.setText(tokenName);
        binding.cymbol.setText(tokenName);

        if (tokenName.equals("ETH")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.eth_icon));
            binding.cymbolHead.setText("ETH");
            binding.cymbol.setText("ETH");
        } else if (tokenName.equals("BNB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.bnb_icon));
            binding.cymbolHead.setText("BNB");
            binding.cymbol.setText("BNB");
        } else if (tokenName.equals("USDT")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdt));
            binding.cymbolHead.setText("USDT");
            binding.cymbol.setText("USDT");
        } else if (tokenName.equals("TTAP")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ttap_new_icon));
            binding.cymbolHead.setText("TTAP");
            binding.cymbol.setText("TTAP");
        } else if (tokenName.equals("DAI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.dai));
            binding.cymbolHead.setText("DAI");
            binding.cymbol.setText("DAI");
        } else if (tokenName.equals("LINK")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.link));
            binding.cymbolHead.setText("LINK");
            binding.cymbol.setText("LINK");
        } else if (tokenName.equals("UNI")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.uni));
            binding.cymbolHead.setText("UNI");
            binding.cymbol.setText("UNI");
        } else if (tokenName.equals("USDC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.usdc));
            binding.cymbolHead.setText("USDC");
            binding.cymbol.setText("USDC");
        } else if (tokenName.equals("WBTC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wbtc));
            binding.cymbolHead.setText("WBTC");
            binding.cymbol.setText("WBTC");
        } else if (tokenName.equals("VEN")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.ven));
            binding.cymbolHead.setText("VEN");
            binding.cymbol.setText("VEN");
        } else if (tokenName.equals("THETA")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.theta));
            binding.cymbolHead.setText("THETA");
            binding.cymbol.setText("THETA");
        } else if (tokenName.equals("WFIL")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.wfil));
            binding.cymbolHead.setText("WFIL");
            binding.cymbol.setText("WFIL");
        } else if (tokenName.equals("BUSD")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.busd));
            binding.cymbolHead.setText("BUSD");
            binding.cymbol.setText("BUSD");
        } else if (tokenName.equals("OKB")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.okb));
            binding.cymbolHead.setText("OKB");
            binding.cymbol.setText("OKB");
        } else if (tokenName.equals("CRO")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cro));
            binding.cymbolHead.setText("CRO");
            binding.cymbol.setText("CRO");
        } else if (tokenName.equals("cUSDC")) {
            binding.logo.setImageDrawable(getResources().getDrawable(R.drawable.cusdc));
            binding.cymbolHead.setText("cUSDC");
            binding.cymbol.setText("cUSDC");
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
                    && !TextUtils.isEmpty(binding.amount.getText().toString())
                    && WalletUtils.isValidAddress(binding.receiverAddress.getText().toString())) {

                if (balance >= fee
                        && Double.parseDouble(binding.totalAmount.getText().toString()) >= Double.parseDouble(binding.amount.getText().toString())) {
                    Intent intent = new Intent(SendActivity.this, VerifySendingActivity.class);
                    intent.putExtra("receiverAddress", binding.receiverAddress.getText().toString());
                    intent.putExtra("fee", String.valueOf(fee));
                    intent.putExtra("tokenName", tokenName);
                    intent.putExtra("contractAddress", contractAddress);
                    intent.putExtra("amount", binding.amount.getText().toString());
                    startActivity(intent);
                } else if (balance < fee){
                    binding.errorMessage.setText("수수료 부족합니다");
                } else if (Double.parseDouble(binding.totalAmount.getText().toString()) < Double.parseDouble(binding.amount.getText().toString())) {
                    binding.errorMessage.setText("잔액이 부족합니다");
                }


            } else if(!WalletUtils.isValidAddress(binding.receiverAddress.getText().toString())) {
                binding.errorMessage.setText("Wallet address is not valid");
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
        }
//        else if (tokenName.equals("BNB")) {
//            ethManager.getTokenBalance(walletAddress, "", ApiUtils.getBnbContractAddress(), this)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(response -> {
//                        binding.totalAmount.setText(response.toString());
//                        totalAmount = Double.parseDouble(response.toString());
//                    }, error -> {
//                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                        System.out.println(error.getMessage());
//                    });
//        }
        else if (tokenName.equals("TTAP")) {
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
        } else {
            ethManager.getTokenBalance(walletAddress, "", contractAddress, this)
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
