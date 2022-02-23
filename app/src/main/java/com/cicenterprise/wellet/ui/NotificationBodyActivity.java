package com.cicenterprise.wellet.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ActivityNotificationBodyBinding;

public class NotificationBodyActivity extends AppCompatActivity {

    ActivityNotificationBodyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_body);

        String date = getIntent().getStringExtra("date");
        String[] pairs = date.split("T");


        binding.backBtn.setOnClickListener(v -> finish());
        binding.text.setText("공지사항");
        binding.title.setText(getIntent().getStringExtra("title"));
        binding.date.setText(pairs[0] + " " + pairs[1].substring(0, 5));
        //binding.date.setText(getIntent().getStringExtra("date"));
        binding.content.setText(getIntent().getStringExtra("content"));
    }
}
