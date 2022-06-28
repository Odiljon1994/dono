package com.richland.wallet.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.databinding.ActivityFaqBinding;
import com.richland.wallet.databinding.ActivityPrivacyPolicyBinding;
import com.richland.wallet.di.ViewModelFactory;
import com.richland.wallet.models.PrivacyPolicyModel;
import com.richland.wallet.ui.viewmodel.PrivacyPolicyVM;
import com.richland.wallet.util.PreferencesUtil;

import javax.inject.Inject;

public class PrivacyPolicyActivity extends AppCompatActivity {

    ActivityPrivacyPolicyBinding binding;


    @Inject
    ViewModelFactory viewModelFactory;
    @Inject
    PreferencesUtil preferencesUtil;
    private ProgressDialog progressDialog;
    PrivacyPolicyVM privacyPolicyVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        privacyPolicyVM = ViewModelProviders.of(this, viewModelFactory).get(PrivacyPolicyVM.class);
        privacyPolicyVM.item().observe(this, this::items);
        privacyPolicyVM.errorMessage().observe(this, this::onError);

       // privacyPolicyVM.getPrivacyPolicy();
       // progressDialog = ProgressDialog.show(this, "", "데이터 불러오는 중…", true);

        binding.backBtn.setOnClickListener(v -> finish());
    }

    public void items(PrivacyPolicyModel model) {

        progressDialog.dismiss();
        if (model.getCode() == 200) {
            binding.content.setText(model.getData().get(0).getContent());
            binding.version.setText(model.getData().get(0).getVersion());
            String date = model.getData().get(0).getCreated_at();
            String[] pairs = date.split("T");
            binding.date.setText(pairs[0] + " " + pairs[1].substring(0, 5));
        }


    }
    public void onError(String error) {

        progressDialog.dismiss();
    }
}
