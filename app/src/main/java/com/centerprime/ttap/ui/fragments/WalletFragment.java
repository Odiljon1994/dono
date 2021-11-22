package com.centerprime.ttap.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.FragmentMainBinding;
import com.centerprime.ttap.databinding.FragmentWalletBinding;

public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        View view = binding.getRoot();

        binding.ttapAsset.setOnClickListener(v -> {
            Fragment someFragment = new TokenFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        return view;

    }

}
