package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.PinLockListener;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityOtpBinding;
import com.centerprime.ttap.util.PreferencesUtil;

import javax.inject.Inject;

public class LoginOTPActivity extends AppCompatActivity {

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
        binding.otpStatus.setText("환영합니다.\n비밀번호를 입력해주세요.");
        binding.firstLine.setText("재방문을 환영합니다.\n비밀번호를 입력해주세요.");
        binding.secondLine.setVisibility(View.GONE);

        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (pin.equals(preferencesUtil.getOtp())) {
                    Intent intent = new Intent(LoginOTPActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    binding.error.setText("비밀번호를 확인해주세요.");
                    binding.pinLockView.resetPinLockView();
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
}
