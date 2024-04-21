package com.mcs.wallet.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.di.ViewModelFactory;
import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ActivityWalletCreatedBinding;
import com.mcs.wallet.models.PostWalletAddressResModel;
import com.mcs.wallet.ui.viewmodel.PostWalletAddressVM;
import com.mcs.wallet.util.PreferencesUtil;

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
