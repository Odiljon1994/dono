package com.cicenterprise.wellet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.cicenterprise.wellet.MyApp;
import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.ActivityWalletCreatedBinding;
import com.cicenterprise.wellet.di.ViewModelFactory;
import com.cicenterprise.wellet.models.PostWalletAddressResModel;
import com.cicenterprise.wellet.ui.viewmodel.PostWalletAddressVM;
import com.cicenterprise.wellet.util.PreferencesUtil;

import javax.inject.Inject;

public class WalletCreatedActivity extends AppCompatActivity {

    private ActivityWalletCreatedBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    private PostWalletAddressVM postWalletAddressVM;
    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        postWalletAddressVM = ViewModelProviders.of(this, viewModelFactory).get(PostWalletAddressVM.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_created);
        postWalletAddressVM.item().observe(this, this::onPosted);

        postWalletAddressVM.postWalletAddress(preferencesUtil.getWalletAddress());


        binding.okBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WalletCreatedActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    public void onPosted(PostWalletAddressResModel postWalletAddressResModel) {
        System.out.println("*****");
        System.out.println(postWalletAddressResModel.getCode());
        System.out.println("*****");
    }
}
