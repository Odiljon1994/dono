package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.andrognito.pinlockview.PinLockListener;
import com.centerprime.ethereum_client_sdk.EthManager;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityOtpBinding;
import com.centerprime.ttap.ui.dialogs.BaseDialog;
import com.centerprime.ttap.util.PreferencesUtil;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.CharsetEncoder;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jnr.ffi.annotations.In;

public class OtpActivity extends AppCompatActivity {
    ActivityOtpBinding binding;
    private boolean isRepeated = false;
    private String OTP = "";
    EthManager ethManager;
    private boolean isDoubleClicked = false;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp);




        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.pinLockView.attachIndicatorDots(binding.indicatorDots);
        ethManager = EthManager.getInstance();
        ethManager.init("https://mainnet.infura.io/v3/7c36e7f5656d4384bbcb2cbaf67ad699");


        binding.pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String pin) {

                if (!isRepeated) {
                    OTP = pin;
                    isRepeated = true;
                    binding.pinLockView.resetPinLockView();
                    binding.otpStatus.setText("다시 한번 더\n입력해 주세요.");
                } else {
                    if (OTP.equals(pin)) {
                        System.out.println(pin);
                        preferencesUtil.saveOtp(pin);


                        ethManager.createWallet(pin, OtpActivity.this)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(walletAddress -> {
                                    System.out.println(walletAddress.getAddress());
                                    showDialog();
                                    preferencesUtil.saveWalletAddress(walletAddress.getAddress());

                                });


                    } else {
                        OTP = "";
                        isRepeated = false;
                        binding.pinLockView.resetPinLockView();
                        binding.otpStatus.setText("6자리 비밀번호를\n입력해주세요.");
                    }
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

//    public void generateAddress() throws CipherException, IOException {
//        System.out.println("Creating New Account");
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//
//        String walletPassword = "121212";
//        /* Define Wallet File Location */
//        String walletPath = this.getFilesDir() + "/" + "wallets";
//        File walletDirectory = new File(String.valueOf(this.getFilesDir()));
//
//        Bip39Wallet walletName = WalletUtils.generateBip39Wallet(walletPassword, walletDirectory);
//        System.out.println("wallet location: " + walletDirectory + "/" + walletName);
//
//        Credentials credentials = WalletUtils.loadBip39Credentials(walletPassword, walletName.getMnemonic());
//        String accountAddress = credentials.getAddress();
//
//        System.out.println("Account address: " + credentials.getAddress());
//
//        preferencesUtil.saveWalletAddress(credentials.getAddress());
//
//
//        ECKeyPair privateKey = credentials.getEcKeyPair();
//
//
//
//        String seedPhrase = walletName.getMnemonic();
//        System.out.println("Account Details:");
//        System.out.println("Your New Account : " + credentials.getAddress());
//        System.out.println("Mneminic Code: " + walletName.getMnemonic());
//        System.out.println(Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
//
//        System.out.println("Private Key: " + privateKey.getPrivateKey().toString(16));
//        System.out.println("Public Key: " + privateKey.getPublicKey().toString(16));
//
//        preferencesUtil.saveMnemonic(walletName.getMnemonic());
//        preferencesUtil.savePrivateKey(Numeric.toHexStringWithPrefix(credentials.getEcKeyPair().getPrivateKey()));
//    }



    private void screenshoot() {
        Date date = new Date();
        CharSequence now = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        String filename = Environment.getExternalStorageDirectory().toString() + "/ScreenShooter/" + now + ".jpg";

        View root = getWindow().getDecorView();
        root.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(root.getDrawingCache());
        root.setDrawingCacheEnabled(false);


        File file = new File(filename);
        file.getParentFile().mkdirs();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "image/*");
            startActivity(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";


            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    public void showDialog() {
        BaseDialog baseDialog = new BaseDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(baseDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        BaseDialog.ClickListener clickListener = new BaseDialog.ClickListener() {
            @Override
            public void onClick() {

                if (!isDoubleClicked) {
                    baseDialog.changeText();
                    isDoubleClicked = true;
                } else {

                    startActivity(new Intent(OtpActivity.this, WalletSeedsActivity.class));
                    dialog.dismiss();
                }

            }
        };
        baseDialog.setClickListener(clickListener);
    }
}
