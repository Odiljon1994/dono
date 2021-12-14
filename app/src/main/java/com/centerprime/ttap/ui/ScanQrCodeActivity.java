package com.centerprime.ttap.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityScanQrcodeBinding;
import com.centerprime.ttap.util.PreferencesUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ScanQrCodeActivity extends AppCompatActivity {
    private ActivityScanQrcodeBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_qrcode);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("QR코드 불러오기");

        createQrCode(preferencesUtil.getMnemonic());

        binding.nextBtn.setOnClickListener(v -> {
            preferencesUtil.saveIsRegistered(true);
            Intent intent = new Intent(ScanQrCodeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


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
