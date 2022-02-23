package com.cicenterprise.wellet.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cicenterprise.wellet.MyApp;
import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.adapter.NotificationAdapter;
import com.cicenterprise.wellet.adapter.NotificationsAdapter;
import com.cicenterprise.wellet.databinding.ActivityNotificationBinding;
import com.cicenterprise.wellet.di.ViewModelFactory;
import com.cicenterprise.wellet.models.NotificationModel;
import com.cicenterprise.wellet.ui.viewmodel.NotificationVM;
import com.cicenterprise.wellet.util.PreferencesUtil;

import javax.inject.Inject;

public class NotificationActivity extends AppCompatActivity {

    ActivityNotificationBinding binding;
    private NotificationAdapter adapter;
    private NotificationsAdapter notificationsAdapter;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private ProgressDialog progressDialog;
    NotificationVM notificationVM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);
        notificationVM = ViewModelProviders.of(this, viewModelFactory).get(NotificationVM.class);
        notificationVM.item().observe(this, this::items);
        notificationVM.errorMessage().observe(this, this::onError);
        binding.toolbar.title.setText("공지사항");
        binding.toolbar.backBtn.setOnClickListener(v -> finish());


        notificationVM.getNotifications();
        progressDialog = ProgressDialog.show(this, "", "데이터 불러오는 중…", true);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        notificationsAdapter = new NotificationsAdapter(this, item -> {
            Intent intent = new Intent(NotificationActivity.this, NotificationBodyActivity.class);
            intent.putExtra("content", item.getContent());
            if (item.getType().equals("1")) {
                intent.putExtra("title", "[공지] " + item.getName());
            } else if (item.getType().equals("2")) {
                intent.putExtra("title", "[이벤트] " + item.getName());
            } else {
                intent.putExtra("title", item.getName());
            }
            intent.putExtra("title", item.getName());
            intent.putExtra("date", item.getCreated_at());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(notificationsAdapter);
//        adapter = new NotificationAdapter(list, this, item -> {
//
//            Intent intent = new Intent(NotificationActivity.this, NotificationBodyActivity.class);
//            intent.putExtra("title", item.getTitle());
//            intent.putExtra("date", item.getDate());
//            startActivity(intent);
//        });
 //       binding.recyclerView.setAdapter(adapter);
    }

    public void items(NotificationModel model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {
            notificationsAdapter.setItems(model.getData());
        }


    }
    public void onError(String error) {

        progressDialog.dismiss();
    }
}
