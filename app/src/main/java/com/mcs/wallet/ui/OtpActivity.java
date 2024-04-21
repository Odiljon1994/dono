package com.mcs.wallet.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.PinLockListener;
import com.mcs.wallet.MyApp;
import com.mcs.wallet.ui.dialogs.BaseDialog;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.MnemonicWallet;
import com.mcs.wallet.R;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.database.DatabaseMainnetToken;
import com.mcs.wallet.databinding.ActivityOtpBinding;
import com.mcs.wallet.web3.EthManager;


import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    private boolean isRepeated = false;
    private String OTP = "";
    EthManager ethManager;
    private boolean isDoubleClicked = false;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        DatabaseMainnetToken databaseMainnetToken = new DatabaseMainnetToken(this);
        boolean isTokenExist = false;

        Cursor data = databaseMainnetToken.getData();

        while (data.moveToNext()) {
            String contractAddress = data.getString(1);
            if (contractAddress.equals("DONOpia")) {
                isTokenExist = true;
            }
        }
        if (!isTokenExist) {
            boolean insetData = databaseMainnetToken.addData("DONOpia", "DONOpia", ApiUtils.getContractAddress());
        }

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
        ethManager = EthManager.getInstance();
        ethManager.init("https://mainnet.infura.io/v3/7c36e7f5656d4384bbcb2cbaf67ad699");


        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {

                if (OTP.equals("")) {
                    if (!hasConsecutiveCharacters(pin)) {
                        OTP = pin;
                        binding.error.setText("");
                        binding.pinLockView.resetPinLockView();
                        binding.otpStatus.setText(R.string.one_more);
                        binding.otpStatusSecondLine.setText(R.string.please_enter);

                        //binding.otpStatus.setText("다시 한번\n입력해 주세요.");
                    } else {
                        binding.pinLockView.resetPinLockView();
                        binding.error.setText(R.string.check_password);
                    }
                } else if (OTP.equals(pin)) {
                    preferencesUtil.saveOtp(pin);

                    MnemonicWallet mnemonicWallet = ethManager.createWalletWithMnemonic();
                    preferencesUtil.saveWalletAddress(mnemonicWallet.getWalletAddress());
                    preferencesUtil.saveMnemonic(mnemonicWallet.getMnemonic());
                    preferencesUtil.savePrivateKey(mnemonicWallet.getPrivateKey());

                    ethManager.importFromPrivateKey(mnemonicWallet.getPrivateKey(), OtpActivity.this)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(walletAddress -> {
                                System.out.println(walletAddress);
                                //showDialog();
                                startActivity(new Intent(OtpActivity.this, WalletSeedsActivity.class));

                            });
                } else if (!OTP.equals("") && !OTP.equals(pin)) {
                    OTP = "";
                    binding.error.setText(R.string.different_password);
                    binding.pinLockView.resetPinLockView();
                    binding.otpStatus.setText(R.string.six_digits);
                    binding.otpStatusSecondLine.setText(R.string.password);
                    //binding.otpStatus.setText("6자리 비밀번호를\n입력해주세요.");
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

    public boolean hasConsecutiveCharacters(String pwd) {
        String[] letter = pwd.split(""); // here you get each letter in to a string array

        for (int i = 0; i < letter.length - 2; i++) {
            if (letter[i].equals(letter[i + 1]) && letter[i + 1].equals(letter[i + 2])) {
                return true; //return true as it has 3 consecutive same character
            }
        }
        return false; //If you reach here that means there are no 3 consecutive characters therefore return false.
    }


    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClick() {

                if (!isDoubleClicked) {
                    baseDialog.changeText("시드 구문을 공유하거나 타 기기에\n저장할 경우 가상자산이 유출될 수 있습니다.");
                    isDoubleClicked = true;
                } else {

                    startActivity(new Intent(OtpActivity.this, WalletSeedsActivity.class));
                    dialog.dismiss();
                }

            }
        };
        baseDialog.setClickListener(clickListener);
    }
}
