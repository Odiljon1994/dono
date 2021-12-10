package com.centerprime.ttap.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.FaqAdapter;
import com.centerprime.ttap.databinding.ActivityFaqBinding;
import com.centerprime.ttap.models.FaqModel;
import com.centerprime.ttap.models.NotificationsModel;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    private ActivityFaqBinding binding;
    private FaqAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);

        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("FAQ");
        List<FaqModel> list = new ArrayList<>();
        list.add(new FaqModel("TTAP Wallet 생성은 어떻게 하나요?"));
        list.add(new FaqModel("TTAP Wallet 생성은 어떻게 하나요?"));
        list.add(new FaqModel("TTAP Wallet 생성은 어떻게 하나요?"));
        list.add(new FaqModel("TTAP Wallet 생성은 어떻게 하나요?"));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new FaqAdapter(this, list, item -> {

            Intent intent = new Intent(FaqActivity.this, FaqBodyActivity.class);
            intent.putExtra("title", item.getFaq());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

    }
}
