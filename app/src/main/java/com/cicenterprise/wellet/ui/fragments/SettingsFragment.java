package com.cicenterprise.wellet.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cicenterprise.wellet.R;
import com.cicenterprise.wellet.databinding.FragmentSettingsBinding;
import com.cicenterprise.wellet.ui.LockAppActivity;
import com.cicenterprise.wellet.ui.OtpExportKeysActivity;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false);
        View view = binding.getRoot();

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

    private void moveToAnotherActivity(String type) {
        Intent intent = new Intent(getActivity(), OtpExportKeysActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}
