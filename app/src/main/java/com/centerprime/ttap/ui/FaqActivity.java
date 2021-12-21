package com.centerprime.ttap.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.adapter.FaqAdapter;
import com.centerprime.ttap.databinding.ActivityFaqBinding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.FaqModel;
import com.centerprime.ttap.models.NotificationModel;
import com.centerprime.ttap.ui.viewmodel.FaqVM;
import com.centerprime.ttap.ui.viewmodel.NotificationVM;
import com.centerprime.ttap.util.PreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FaqActivity extends AppCompatActivity {

    private ActivityFaqBinding binding;
    private FaqAdapter adapter;
    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private ProgressDialog progressDialog;
    FaqVM faqVM;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);
        faqVM = ViewModelProviders.of(this, viewModelFactory).get(FaqVM.class);
        faqVM.item().observe(this, this::items);
        faqVM.errorMessage().observe(this, this::onError);

        faqVM.getFaq();
        progressDialog = ProgressDialog.show(this, "", "데이터 불러오는 중…", true);
        binding.toolbar.backBtn.setOnClickListener(v -> finish());
        binding.toolbar.title.setText("FAQ");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new FaqAdapter(this, item -> {

            Intent intent = new Intent(FaqActivity.this, FaqBodyActivity.class);
            intent.putExtra("title", item.getTitle());
            intent.putExtra("date", item.getCreated_at());
            intent.putExtra("content", item.getContent());
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(adapter);

    }

    public void items(FaqModel model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {
            adapter.setItems(model.getData());
        }


    }
    public void onError(String error) {

        progressDialog.dismiss();
    }
}
