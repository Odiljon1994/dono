package com.centerprime.ttap.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.databinding.FragmentMain2Binding;
import com.centerprime.ttap.databinding.FragmentMain3Binding;
import com.centerprime.ttap.di.ViewModelFactory;
import com.centerprime.ttap.models.BannerListModel;
import com.centerprime.ttap.models.NotificationModel;
import com.centerprime.ttap.ui.NotificationActivity;
import com.centerprime.ttap.ui.NotificationBodyActivity;
import com.centerprime.ttap.ui.WebViewActivity;
import com.centerprime.ttap.ui.viewmodel.BannersListVM;
import com.centerprime.ttap.ui.viewmodel.NotificationVM;
import com.centerprime.ttap.util.PreferencesUtil;
import com.centerprime.ttap.web3.EthManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainFragment3 extends Fragment {

    FragmentMain3Binding binding;
    private String walletAddress;
    private String[] notificationTitles = {"[공지] 가상자산 사업자 관련하여 공식 입장 안내...", "[공지] 가상자산 사업자 관련하여 공식 입장 안내...", "[공지] 가상자산 사업자 관련하여 공식 입장 안내..."};
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private ProgressDialog progressDialog;
    NotificationVM notificationVM;
    BannersListVM bannersListVM;
    private String[] fadingTitles;
    private List<NotificationModel.Data> list = new ArrayList<>();
    private String firstBannerUrl = "";
    private String secondBannerUrl = "";
    private String thirdBannerUrl = "";
    private  String fourthBannerUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main3, container, false);
        notificationVM = ViewModelProviders.of(this, viewModelFactory).get(NotificationVM.class);
        bannersListVM = ViewModelProviders.of(this, viewModelFactory).get(BannersListVM.class);
        notificationVM.item().observe(getActivity(), this::items);
        notificationVM.errorMessage().observe(getActivity(), this::onError);
        bannersListVM.item().observe(getActivity(), this::bannersList);
        bannersListVM.itemOnError().observe(getActivity(), this::onError);
        View view = binding.getRoot();

        binding.notificationTitles.setTexts(notificationTitles);
        binding.notificationTitles.setOnClickListener(v -> {

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().equals(binding.notificationTitles.getText().toString())) {
                    Intent intent = new Intent(getActivity(), NotificationBodyActivity.class);

                    intent.putExtra("content", list.get(i).getContent());
                    intent.putExtra("title", list.get(i).getName());
                    intent.putExtra("date", list.get(i).getCreated_at());
                    startActivity(intent);
                    break;
                }
            }


        });

        binding.firstBanner.setOnClickListener(v -> {
            try {
                URL url = new URL(firstBannerUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(firstBannerUrl));
                startActivity(browserIntent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });
        binding.bannerSecond.setOnClickListener(v -> {
            try {
                URL url = new URL(secondBannerUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(secondBannerUrl));
                startActivity(browserIntent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        binding.bannerThird.setOnClickListener(v -> {
            try {
                URL url = new URL(thirdBannerUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(thirdBannerUrl));
                startActivity(browserIntent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        binding.bannerFourth.setOnClickListener(v -> {
            try {
                URL url = new URL(fourthBannerUrl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fourthBannerUrl));
                startActivity(browserIntent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        walletAddress = preferencesUtil.getWalletAddress();
        binding.progressBar.getProgress();

        checkBalance();
        notificationVM.getNotifications();
        bannersListVM.getBannerList();

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

    public void bannersList(BannerListModel model) {
        if (model.getCode() == 200) {
            for (BannerListModel.BannerListData i : model.getData()) {
                if (i.getPosition() == 1) {
                    firstBannerUrl = i.getExternal_url();
                    Glide.with(this)
                            .load(i.getFile_url())
                            .centerCrop()
                            .into(binding.firstBanner);
                } else if (i.getPosition() == 2) {
                    secondBannerUrl = i.getExternal_url();
                    Glide.with(this)
                            .load(i.getFile_url())
                            .centerCrop()
                            .into(binding.bannerSecond);
                } else if (i.getPosition() == 3) {
                    thirdBannerUrl = i.getExternal_url();
                    Glide.with(this)
                            .load(i.getFile_url())
                            .centerCrop()
                            .into(binding.bannerThird);
                } else if (i.getPosition() == 4) {
                    fourthBannerUrl = i.getExternal_url();
                    Glide.with(this)
                            .load(i.getFile_url())
                            .centerCrop()
                            .into(binding.bannerFourth);
                }
            }
        }
    }

    public void items(NotificationModel model) {


        if (model.getCode() == 200) {
            fadingTitles = new String[model.getData().size()];
            for (int i = 0; i < fadingTitles.length; i++) {

                String title = "";
                if (model.getData().get(i).getType().equals("1")) {
                    title = "[공지] " + model.getData().get(i).getName();
                }else if (model.getData().get(i).getType().equals("2")) {
                    title = "[이벤트] " + model.getData().get(i).getName();
                } else {
                    title = model.getData().get(i).getName();
                }


                list.add(new NotificationModel.Data(model.getData().get(i).getId(),
                        title,
                        model.getData().get(i).getType(),
                        model.getData().get(i).getContent(),
                        model.getData().get(i).getUpdated_at(),
                        model.getData().get(i).getCreated_at()));

                fadingTitles[i] = title;

            }
            binding.notificationTitles.setTexts(fadingTitles);
        }


    }
    public void onError(String error) {


    }
}
