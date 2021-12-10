package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.NotificationAdapter;
import com.centerprime.ttap.databinding.ActivityNotificationBinding;
import com.centerprime.ttap.models.AddressesBookModel;
import com.centerprime.ttap.models.NotificationsModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);

        binding.toolbar.title.setText("공지사항");
        binding.toolbar.backBtn.setOnClickListener(v -> finish());

        List<NotificationsModel> list = new ArrayList<>();
        list.add(new NotificationsModel("[공지] TTAP Wallet 출시!", "2021.11.19"));
        list.add(new NotificationsModel("[긴급] 에어드랍", "2021.11.19"));
        list.add(new NotificationsModel("[공지] TTAP Wallet 출시! 이벤트", "2021.11.19"));
        list.add(new NotificationsModel("[공지] 출시 임박", "2021.11.19"));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new NotificationAdapter(list, this, item -> {

            Intent intent = new Intent(NotificationActivity.this, NotificationBodyActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getDate());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);
    }
}
