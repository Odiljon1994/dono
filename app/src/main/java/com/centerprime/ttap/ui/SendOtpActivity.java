package com.centerprime.ttap.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.PinLockListener;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.databinding.ActivityOtpBinding;
import com.centerprime.ttap.util.Constants;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SendOtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;
    private EthManager ethManager;
    private String receiver;
    private String amount;
    private String fee;
    private String walletAddress;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
        System.out.println(preferencesUtil.getOtp());

        receiver = getIntent().getStringExtra("receiverAddress");
        amount = getIntent().getStringExtra("amount");
        //double getFee = getIntent().getDoubleExtra("fee", 0);
        fee = getIntent().getStringExtra("fee");
        walletAddress = preferencesUtil.getWalletAddress();

        ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());
        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                BigInteger gasPrice = new BigInteger("30000000000");
                BigInteger gasLimit = new BigInteger("21000");
                BigInteger gasLimitToken = new BigInteger("150000");
                BigDecimal tokenAmount = new BigDecimal(amount);
                if(preferencesUtil.getOtp().equals(pin)) {
                    progressDialog = ProgressDialog.show(SendOtpActivity.this, "Loading", "", true);

                    ethManager.sendToken(walletAddress,
                            "",
                            gasPrice,
                            gasLimitToken,
                            tokenAmount,
                            receiver,
                            ApiUtils.getContractAddress(),
                            SendOtpActivity.this)

                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(tx -> {

                                sendFee();
//                                progressDialog.dismiss();
//                                Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Toast.makeText(SendOtpActivity.this, "Success: " + tx, Toast.LENGTH_SHORT).show();
//                                startActivity(intent);

                            }, error -> {
                                progressDialog.dismiss();
                                Toast.makeText(SendOtpActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                } else {
                    binding.pinLockView.resetPinLockView();
                    Toast.makeText(SendOtpActivity.this, "Password error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {

            }
        });
    }

    public void sendFee() {
        BigInteger gasPrice = new BigInteger("30000000000");
        BigInteger gasLimit = new BigInteger("21000");
        BigInteger gasLimitToken = new BigInteger("150000");
        BigDecimal tokenAmount = new BigDecimal(fee);
        ethManager.sendToken(preferencesUtil.getWalletAddress(),
                "",
                gasPrice,
                gasLimitToken,
                tokenAmount,
                Constants.FEE_RECEIVER_ADDRESS,
                ApiUtils.getContractAddress(),
                SendOtpActivity.this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tx -> {
                    progressDialog.dismiss();
                    Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(SendOtpActivity.this, "Success: " + tx, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }, error -> {
                    progressDialog.dismiss();
                    Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(SendOtpActivity.this, "Error on getting fee: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                });
    }
}
