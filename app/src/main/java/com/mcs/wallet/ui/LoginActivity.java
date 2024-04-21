package com.mcs.wallet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ActivityLockAppBinding;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private ActivityLockAppBinding binding;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lock_app);


        binding.goOtp.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, LoginOTPActivity.class)));

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                binding.error.setText("error " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
               // binding.fingerprintImage.setImageDrawable(getDrawable(R.drawable.fingerprint_success));
                binding.fingerprintImage.setImageDrawable(getResources().getDrawable(R.drawable.fingerprint_success));


                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                binding.error.setText("failed");
            }
        });



        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Bio Authentication")
                .setSubtitle("Login using fingerprint")
                .setNegativeButtonText("Use app password")
                .build();

        biometricPrompt.authenticate(promptInfo);

        binding.okBtn.setOnClickListener(v -> {

        });

    }
}
