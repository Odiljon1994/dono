package com.centerprime.ttap.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityQrcodeBackupBinding;
import com.centerprime.ttap.util.PreferencesUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BackUpQrCodeActivity extends AppCompatActivity {
    private ActivityQrcodeBackupBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrcode_backup);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("QR 코드 백업");

        createQrCode(preferencesUtil.getMnemonic());


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
