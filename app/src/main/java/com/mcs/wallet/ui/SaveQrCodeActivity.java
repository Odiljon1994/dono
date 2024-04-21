package com.mcs.wallet.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ActivitySaveQrcodeBinding;
import com.mcs.wallet.ui.dialogs.ScreenSHotDialog;
import com.mcs.wallet.util.PreferencesUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SaveQrCodeActivity extends AppCompatActivity {
    ActivitySaveQrcodeBinding binding;
    private boolean isConfirmed = false;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_save_qrcode);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        createQrCode(getIntent().getStringExtra("seeds"));


        binding.okBtn.setOnClickListener(v -> {
            showDialog();
        });
    }

    public void showDialog() {
        ScreenSHotDialog screenSHotDialog = new ScreenSHotDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(screenSHotDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        ScreenSHotDialog.ClickListener clickListener = new ScreenSHotDialog.ClickListener() {
            @Override
            public void onClickOk() {
                if (!isConfirmed) {
                    screenSHotDialog.confirmScreenShot();
                    isConfirmed = true;
                } else {

                    Intent intent = new Intent(SaveQrCodeActivity.this, MainActivity.class);

                    preferencesUtil.saveIsRegistered(true);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    dialog.dismiss();
                }

            }

            @Override
            public void onClickNo() {
                dialog.dismiss();
            }
        };
        screenSHotDialog.setClickListener(clickListener);
    }

    public void createQrCode(String fullWallet) {

        createQRImage(fullWallet)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        image -> binding.qrcode.setImageBitmap(image));
    }

    private Single<Bitmap> createQRImage(String address) {
        return Single.fromCallable(() -> {
            try {
                BitMatrix bitMatrix = new MultiFormatWriter().encode(
                        address,
                        BarcodeFormat.QR_CODE,
                        300,
                        300,
                        null);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                return barcodeEncoder.createBitmap(bitMatrix);
            } catch (Exception e) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT)
                        .show();
            }
            return null;
        });
    }
}