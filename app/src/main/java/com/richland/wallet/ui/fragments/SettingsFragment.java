package com.richland.wallet.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.databinding.FragmentSettingsBinding;
import com.richland.wallet.ui.LockAppActivity;
import com.richland.wallet.ui.OtpExportKeysActivity;
import com.richland.wallet.ui.WalletActivity;
import com.richland.wallet.util.PreferencesUtil;

import java.util.Locale;

import javax.inject.Inject;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    @Inject
    PreferencesUtil preferencesUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View view = binding.getRoot();

        if (preferencesUtil.getLANGUAGE().equals("ja")) {
            binding.isJp.setChecked(true);

        } else {
            binding.isKor.setChecked(true);
        }

        binding.isJp.setOnClickListener(v -> {
            binding.isKor.setChecked(false);
            binding.isJp.setChecked(true);
            setAppLocale("ja");
            preferencesUtil.saveLanguage("ja");
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.this).commit();
            // recreate();
        });

        binding.isKor.setOnClickListener(v -> {
            binding.isJp.setChecked(false);
            binding.isKor.setChecked(true);
            setAppLocale("en");
            preferencesUtil.saveLanguage("en");
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SettingsFragment.this).commit();
            //   recreate();
        });


        binding.qrCodeBackUp.setOnClickListener(v -> {
            moveToAnotherActivity("qrCode");
        });

        binding.privateKey.setOnClickListener(v -> {
            moveToAnotherActivity("privateKey");
        });

        binding.seedBackUp.setOnClickListener(v -> {
            moveToAnotherActivity("seeds");
        });

        binding.lockTheWallet.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LockAppActivity.class));
        });

        return view;
    }

    private void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }

    private void moveToAnotherActivity(String type) {
        Intent intent = new Intent(getActivity(), OtpExportKeysActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
