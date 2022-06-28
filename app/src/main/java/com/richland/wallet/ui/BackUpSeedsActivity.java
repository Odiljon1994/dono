package com.richland.wallet.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.databinding.ActivityWalletSeedsBinding;
import com.richland.wallet.ui.dialogs.SeedBackupDialog;
import com.richland.wallet.util.PreferencesUtil;

import javax.inject.Inject;

public class BackUpSeedsActivity extends AppCompatActivity {
    private boolean isConfirmed = false;
    ActivityWalletSeedsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_seeds);

        binding.backBtn.setOnClickListener(v -> finish());

        binding.seeds.setText(preferencesUtil.getMnemonic());

        binding.okBtn.setOnClickListener(v -> {
            showDialog();
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

                Intent intent = new Intent(BackUpSeedsActivity.this, MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
