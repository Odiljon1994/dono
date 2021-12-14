package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.PinLockListener;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityOtpBinding;
import com.centerprime.ttap.util.PreferencesUtil;

import javax.inject.Inject;

public class OtpImportWalletActivity extends AppCompatActivity {

    private ActivityOtpBinding binding;
    private String OTP = "";
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);

        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {


                if (OTP.equals("")) {
                    if (!hasConsecutiveCharacters(pin)) {
                        OTP = pin;
                        binding.error.setText("");
                        binding.pinLockView.resetPinLockView();
                        binding.otpStatus.setText("다시 한번 더\n입력해 주세요.");
                    } else {
                        binding.pinLockView.resetPinLockView();
                        binding.error.setText("비밀번호를 확인해주세요.");
                    }
                } else if (OTP.equals(pin)) {
                    preferencesUtil.saveOtp(pin);
                    startActivity(new Intent(OtpImportWalletActivity.this, WalletImportedActivity.class));

                } else if (!OTP.equals("") && !OTP.equals(pin)) {
                    OTP = "";
                    binding.error.setText("입력한 비밀번호가 다릅니다.");
                    binding.pinLockView.resetPinLockView();
                    binding.otpStatus.setText("6자리 비밀번호를\n입력해주세요.");
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
}
