package com.centerprime.ttap.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.util.PreferencesUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

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
