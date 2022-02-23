package com.cicenterprise.wellet.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cicenterprise.wellet.MyApp;
import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.util.PreferencesUtil;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> {


            if (!preferencesUtil.getIsRegistered()) {
                startActivity(new Intent(SplashActivity.this, WalletActivity.class));
            } else if (preferencesUtil.getIsAppLocked()) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            finish();

        }, 1500);
    }
}
