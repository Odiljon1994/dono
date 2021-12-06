package com.centerprime.ttap.ui;

import android.content.Intent;
import android.graphics.Color;
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

public class OtpExportKeysActivity extends AppCompatActivity {
    private ActivityOtpBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);

        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.otpStatus.setText("비밀번호\n입력");
        binding.blueText.setText("");
        String type = getIntent().getStringExtra("type");

        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (preferencesUtil.getOtp().equals(pin)) {

                    if (type.equals("qrCode")) {
                        startActivity(new Intent(OtpExportKeysActivity.this, BackUpQrCodeActivity.class));
                    } else if (type.equals("seeds")) {
                        startActivity(new Intent(OtpExportKeysActivity.this, BackUpSeedsActivity.class));
                    } else if (type.equals("privateKey")) {

                    }

                } else {
                    binding.pinLockView.resetPinLockView();
                    binding.blueText.setTextColor(Color.parseColor("#C80000"));
                    binding.blueText.setText("비밀번호를 확인해주세요.");
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
