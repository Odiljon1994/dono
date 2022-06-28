package com.richland.wallet.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.util.PreferencesUtil;

import java.util.Locale;

import javax.inject.Inject;

public class SplashActivity extends AppCompatActivity {
    @Inject
    PreferencesUtil preferencesUtil;
    boolean isJP = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_splash);

        if(preferencesUtil.getLANGUAGE().equals("")){
            preferencesUtil.saveLanguage("en");
        }
        isJP = (preferencesUtil.getLANGUAGE().equals("ja") ? true : false);
        setAppLocale(preferencesUtil.getLANGUAGE());

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

    private void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
