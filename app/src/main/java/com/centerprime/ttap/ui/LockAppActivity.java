package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityLockAppBinding;
import com.centerprime.ttap.ui.dialogs.FingerprintDialog;
import com.centerprime.ttap.ui.dialogs.ScreenSHotDialog;
import com.centerprime.ttap.util.PreferencesUtil;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class LockAppActivity extends AppCompatActivity {
    private ActivityLockAppBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lock_app);

        showDialog();

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LockAppActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                binding.error.setText("error " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //binding.fingerprintImage.setImageDrawable(getDrawable(R.drawable.fingerprint_success));
                binding.fingerprintImage.setImageDrawable(getResources().getDrawable(R.drawable.fingerprint_success));
                preferencesUtil.saveIsAppLocked(true);
                Intent intent = new Intent(LockAppActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                binding.error.setText("failed");
            }
        });

        binding.goOtp.setOnClickListener(v -> {
            startActivity(new Intent(LockAppActivity.this, LockOtpActivity.class));
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Bio Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Use app password")
                .build();
    }

    public void showDialog() {
        FingerprintDialog fingerprintDialog = new FingerprintDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(fingerprintDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ScreenSHotDialog.ClickListener clickListener = new ScreenSHotDialog.ClickListener() {
            @Override
            public void onClickOk() {
                biometricPrompt.authenticate(promptInfo);
                dialog.dismiss();
            }

            @Override
            public void onClickNo() {
                dialog.dismiss();
                finish();
            }
        };
        fingerprintDialog.setClickListener(clickListener);
    }
}
