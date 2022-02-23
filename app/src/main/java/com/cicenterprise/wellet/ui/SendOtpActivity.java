package com.cicenterprise.wellet.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.andrognito.pinlockview.PinLockListener;
import com.cicenterprise.wellet.MyApp;
import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.api.ApiUtils;
import com.cicenterprise.wellet.databinding.ActivityOtpBinding;
import com.cicenterprise.wellet.di.ViewModelFactory;
import com.cicenterprise.wellet.models.PostTransactionResModel;
import com.cicenterprise.wellet.ui.viewmodel.PostTransactionVM;
import com.cicenterprise.wellet.util.Constants;
import com.cicenterprise.wellet.util.PreferencesUtil;
import com.cicenterprise.wellet.web3.EthManager;

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
    private String tokenName;
    private String contractAddress;
    private String walletAddress;
    private ProgressDialog progressDialog;
    private PostTransactionVM postTransactionVM;
    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);
        postTransactionVM = ViewModelProviders.of(this, viewModelFactory).get(PostTransactionVM.class);
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
        postTransactionVM.item().observe(this, this::items);
        postTransactionVM.itemOnError().observe(this, this::onError);
        System.out.println(preferencesUtil.getOtp());

        receiver = getIntent().getStringExtra("receiverAddress");
        amount = getIntent().getStringExtra("amount");
        tokenName = getIntent().getStringExtra("tokenName");
        contractAddress = getIntent().getStringExtra("contractAddress");
        //double getFee = getIntent().getDoubleExtra("fee", 0);
        fee = getIntent().getStringExtra("fee");
        walletAddress = preferencesUtil.getWalletAddress();

        ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());
        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {

                BigInteger gasPrice = new BigInteger("100000000000");
                BigInteger gasPriceToken = new BigInteger("100000000000");
                BigInteger gasLimit = new BigInteger("21000");
                BigInteger gasLimitToken = new BigInteger("120000");
                BigDecimal tokenAmount = new BigDecimal(amount);
                if(preferencesUtil.getOtp().equals(pin)) {
                    progressDialog = ProgressDialog.show(SendOtpActivity.this, "Loading", "", true);

                    binding.otpStatus.setText("출금 요청이\n완료되었습니다.");
                    binding.onSendingText.setVisibility(View.VISIBLE);
                    if (tokenName.equals("ETH")) {



                        ethManager.sendEther(walletAddress, "", gasPrice, gasLimit, tokenAmount, receiver, SendOtpActivity.this)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(tx -> {

                                    System.out.println("** Sending ether is success");
                                    sendFee();
//                                progressDialog.dismiss();
//                                Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Toast.makeText(SendOtpActivity.this, "Success: " + tx, Toast.LENGTH_SHORT).show();
//                                startActivity(intent);

                                }, error -> {
                                    progressDialog.dismiss();
                                    System.out.println("** error on sending eth: " + error.getMessage());
                                    Toast.makeText(SendOtpActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        ethManager.sendToken(walletAddress,
                                "",
                                gasPriceToken,
                                gasLimitToken,
                                tokenAmount,
                                receiver,
                                contractAddress,
                                SendOtpActivity.this)

                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(tx -> {
                                    System.out.println("** Sending token is success");
                                    sendFee();
//                                progressDialog.dismiss();
//                                Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Toast.makeText(SendOtpActivity.this, "Success: " + tx, Toast.LENGTH_SHORT).show();
//                                startActivity(intent);

                                }, error -> {
                                    System.out.println("** error on sending token: " + error.getMessage());
                                    progressDialog.dismiss();
                                    Toast.makeText(SendOtpActivity.this, "error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }

                } else {
                    binding.pinLockView.resetPinLockView();
                    binding.error.setText("비밀번호를 확인해주세요.");
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
        BigInteger gasPrice = new BigInteger("100000000000");
        BigInteger gasLimit = new BigInteger("21000");
        BigInteger gasLimitToken = new BigInteger("120000");
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
                    System.out.println("** Sending fee token is success");
                    progressDialog.dismiss();
                    postTransactionVM.postTransaction(fee);
                    Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(SendOtpActivity.this, "Success: " + tx, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }, error -> {
                    System.out.println("** Error on sending fee: " + error.getMessage());
                    progressDialog.dismiss();
                    Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(SendOtpActivity.this, "Error on getting fee: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                });
    }

    public void items(PostTransactionResModel model) {
        System.out.println("*****");
        System.out.println("** Post transaction response: " + model.getStatus());
        System.out.println("*****");
//        Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

//        startActivity(intent);

    }
    public void onError(String error) {
        System.out.println("*****");
        System.out.println("** Post transaction error: " + error);
        System.out.println("*****");
//        Intent intent = new Intent(SendOtpActivity.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(intent);
    }
}
