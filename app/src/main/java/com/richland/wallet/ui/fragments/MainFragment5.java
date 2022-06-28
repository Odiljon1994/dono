package com.richland.wallet.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.richland.wallet.MyApp;
import com.richland.wallet.R;
import com.richland.wallet.adapter.SearchSectionsAdapter;
import com.richland.wallet.api.ApiUtils;
import com.richland.wallet.databinding.FragmentMain4Binding;
import com.richland.wallet.databinding.FragmentMain5Binding;
import com.richland.wallet.di.ViewModelFactory;
import com.richland.wallet.models.BannerListModel;
import com.richland.wallet.models.FaqModel;
import com.richland.wallet.models.NotificationModel;
import com.richland.wallet.models.SearchSectionModel;
import com.richland.wallet.ui.AddressesBookActivity;
import com.richland.wallet.ui.DirectQuestionActivity;
import com.richland.wallet.ui.FaqActivity;
import com.richland.wallet.ui.FaqBodyActivity;
import com.richland.wallet.ui.NotificationActivity;
import com.richland.wallet.ui.NotificationBodyActivity;
import com.richland.wallet.ui.PrivacyPolicyActivity;
import com.richland.wallet.ui.TermsOfUseActivity;
import com.richland.wallet.ui.viewmodel.BannersListVM;
import com.richland.wallet.ui.viewmodel.FaqVM;
import com.richland.wallet.ui.viewmodel.NotificationVM;
import com.richland.wallet.util.PreferencesUtil;
import com.richland.wallet.web3.EthManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainFragment5 extends Fragment {
    FragmentMain5Binding binding;
    private String walletAddress;

    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    
    private SearchSectionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main5, container, false);

        View view = binding.getRoot();


        walletAddress = preferencesUtil.getWalletAddress();
        checkBalance();
        binding.progressBar.getProgress();

        binding.refreshBtn.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            checkBalance();

        });

        return view;
    }

    public void checkBalance() {

        EthManager ethManager = EthManager.getInstance();
        ethManager.init(ApiUtils.getInfura());

        ethManager.getTokenBalance(walletAddress, "", ApiUtils.getContractAddress(), getActivity())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(balance -> {
                    binding.amount.setText(balance.toString());
                    binding.progressBar.setVisibility(View.GONE);

                }, error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    // Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());

                });
    }
}
