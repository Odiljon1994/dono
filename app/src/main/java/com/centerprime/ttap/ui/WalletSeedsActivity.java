package com.centerprime.ttap.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityWalletSeedsBinding;
import com.centerprime.ttap.ui.dialogs.ScreenSHotDialog;
import com.centerprime.ttap.ui.dialogs.SeedBackupDialog;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jnr.ffi.annotations.In;

public class WalletSeedsActivity extends AppCompatActivity {
    private ActivityWalletSeedsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_seeds);

        binding.backBtn.setOnClickListener(v -> finish());

        binding.seeds.setText(preferencesUtil.getMnemonic());

        binding.okBtn.setOnClickListener(v -> {

            showDialog();
//            Intent intent = new Intent(WalletSeedsActivity.this, SaveQrCodeActivity.class);
//            intent.putExtra("seeds", binding.seeds.getText().toString());
//            startActivity(intent);
        });
    }

    public void showDialog() {
        SeedBackupDialog seedBackupDialog = new SeedBackupDialog(this);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(seedBackupDialog);
        AlertDialog dialog = alertBuilder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        SeedBackupDialog.ClickListener clickListener = new SeedBackupDialog.ClickListener() {
            @Override
            public void onClickOk() {
//                if (!isConfirmed) {
//                    screenSHotDialog.confirmScreenShot();
//                    isConfirmed = true;
//                } else {

                Intent intent = new Intent(WalletSeedsActivity.this, WalletCreatedActivity.class);
                preferencesUtil.saveIsRegistered(true);
              //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
                //   }

            }

            @Override
            public void onClickNo() {
                dialog.dismiss();
            }
        };
        seedBackupDialog.setClickListener(clickListener);
    }
}
