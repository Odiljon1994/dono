package com.cicenterprise.wellet.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cicenterprise.wellet.MyApp;
import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ActivityExportPrivatekeyBinding;
import com.cicenterprise.wellet.util.PreferencesUtil;

import javax.inject.Inject;

public class ExportPrivatekeyActivity extends AppCompatActivity {

    ActivityExportPrivatekeyBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_export_privatekey);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("프라이빗 키");
        binding.privateKey.setText(preferencesUtil.getPrivateKey());

        binding.copyBtn.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("", binding.privateKey.getText().toString());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show();
        });

        binding.okBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });


    }
}
