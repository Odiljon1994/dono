package com.richland.wallet.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.PinLockListener;
import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.databinding.ActivityOtpBinding;
import com.richland.wallet.util.PreferencesUtil;

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
        binding.otpStatus.setText(R.string.password);
        binding.otpStatusSecondLine.setText(R.string.please_enter);
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
                        startActivity(new Intent(OtpExportKeysActivity.this, ExportPrivatekeyActivity.class));
                    }

                } else {
                    binding.pinLockView.resetPinLockView();
                    binding.blueText.setTextColor(Color.parseColor("#C80000"));
                    binding.blueText.setText(R.string.check_password);
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
