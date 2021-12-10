package com.centerprime.ttap.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityFaqBodyBinding;

public class FaqBodyActivity extends AppCompatActivity {

    private ActivityFaqBodyBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq_body);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.title.setText(getIntent().getStringExtra("title"));
    }
}
