package com.richland.wallet.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.richland.wallet.R;
import com.richland.wallet.databinding.ActivityWebviewBinding;

public class WebViewActivity extends AppCompatActivity {
    ActivityWebviewBinding binding;
    String url = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        url = getIntent().getStringExtra("url");
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.webView.loadUrl(url);
    }
}
