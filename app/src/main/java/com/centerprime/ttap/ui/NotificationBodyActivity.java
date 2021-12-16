package com.centerprime.ttap.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityNotificationBodyBinding;

public class NotificationBodyActivity extends AppCompatActivity {

    ActivityNotificationBodyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_body);

        binding.backBtn.setOnClickListener(v -> finish());
        binding.text.setText("공지사항");
        binding.title.setText(getIntent().getStringExtra("title"));
        binding.date.setText(getIntent().getStringExtra("date"));
        binding.content.setText(getIntent().getStringExtra("content"));
    }
}
