package com.mcs.wallet.ui.fragments;

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
import com.mcs.wallet.MyApp;
import com.mcs.wallet.di.ViewModelFactory;
import com.mcs.wallet.ui.AddressesBookActivity;
import com.mcs.wallet.ui.FaqBodyActivity;
import com.mcs.wallet.ui.NotificationBodyActivity;
import com.mcs.wallet.ui.PrivacyPolicyActivity;
import com.mcs.wallet.ui.TermsOfUseActivity;
import com.mcs.wallet.R;
import com.mcs.wallet.adapter.SearchSectionsAdapter;
import com.mcs.wallet.api.ApiUtils;
import com.mcs.wallet.databinding.FragmentMain4Binding;
import com.mcs.wallet.models.BannerListModel;
import com.mcs.wallet.models.FaqModel;
import com.mcs.wallet.models.NotificationModel;
import com.mcs.wallet.models.SearchSectionModel;
import com.mcs.wallet.ui.FaqActivity;
import com.mcs.wallet.ui.NotificationActivity;
import com.mcs.wallet.ui.viewmodel.BannersListVM;
import com.mcs.wallet.ui.viewmodel.FaqVM;
import com.mcs.wallet.ui.viewmodel.NotificationVM;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.web3.EthManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainFragment4 extends Fragment {

    FragmentMain4Binding binding;
    private String walletAddress;
    private String[] notificationTitles = {"[공지] 가상자산 사업자 관련하여 공식 입장 안내...", "[공지] 가상자산 사업자 관련하여 공식 입장 안내...", "[공지] 가상자산 사업자 관련하여 공식 입장 안내..."};
    private List<SearchSectionModel> searchSectionModels;
    private List<SearchSectionModel> filteredSearchSectionModels;
    @Inject
    PreferencesUtil preferencesUtil;
    @Inject
    ViewModelFactory viewModelFactory;
    private ProgressDialog progressDialog;
    NotificationVM notificationVM;
    FaqVM faqVM;
    BannersListVM bannersListVM;
    private String[] fadingTitles;
    private List<NotificationModel.Data> notificationList = new ArrayList<>();
    private List<FaqModel.FaqData> faqList = new ArrayList<>();
    private String firstBannerUrl = "";
    private String secondBannerUrl = "";
    private String thirdBannerUrl = "";
    private String fourthBannerUrl = "";
    private SearchSectionsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((MyApp) getActivity().getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main4, container, false);
        notificationVM = ViewModelProviders.of(this, viewModelFactory).get(NotificationVM.class);
        bannersListVM = ViewModelProviders.of(this, viewModelFactory).get(BannersListVM.class);
        faqVM = ViewModelProviders.of(this, viewModelFactory).get(FaqVM.class);
        notificationVM.item().observe(getActivity(), this::items);
        notificationVM.errorMessage().observe(getActivity(), this::onError);
        faqVM.item().observe(getActivity(), this::faqItems);
        faqVM.errorMessage().observe(getActivity(), this::onFaqError);

        bannersListVM.item().observe(getActivity(), this::bannersList);
        bannersListVM.itemOnError().observe(getActivity(), this::onError);
        View view = binding.getRoot();

        notificationVM.getNotifications();
        faqVM.getFaq();
        bannersListVM.getBannerList();

        binding.closeBtn.setOnClickListener(v -> {
            binding.recyclerView.setVisibility(View.GONE);
            binding.closeBtn.setVisibility(View.GONE);
            binding.searchEditText.clearFocus();
            binding.noItem.setVisibility(View.GONE);
        });

        searchSectionModels = new ArrayList<>();
        searchSectionModels.add(new SearchSectionModel("other", "공지사항", 0));
        searchSectionModels.add(new SearchSectionModel("other", "FAQ", 0));
        searchSectionModels.add(new SearchSectionModel("other", "주소록", 0));
        searchSectionModels.add(new SearchSectionModel("other", "이용약관", 0));
        searchSectionModels.add(new SearchSectionModel("other", "개인정보보호처리방침", 0));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        adapter = new SearchSectionsAdapter(getActivity(), searchSectionModels, item -> {

            binding.recyclerView.setVisibility(View.GONE);
            binding.closeBtn.setVisibility(View.GONE);
            binding.searchEditText.setText(item.getTitle());
            if (item.getType().equals("other") && item.getTitle().equals("주소록")) {
                startActivity(new Intent(getActivity(), AddressesBookActivity.class));
            } else if (item.getType().equals("other") && item.getTitle().equals("공지사항")) {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            } else if (item.getType().equals("other") && item.getTitle().equals("FAQ")) {
                startActivity(new Intent(getActivity(), FaqActivity.class));
            } else if (item.getType().equals("other") && item.getTitle().equals("주소록")) {
                startActivity(new Intent(getActivity(), AddressesBookActivity.class));
            } else if (item.getType().equals("other") && item.getType().equals("other") && item.getTitle().equals("이용약관")) {
                startActivity(new Intent(getActivity(), TermsOfUseActivity.class));
            } else if (item.getType().equals("other") && item.getType().equals("other") && item.getTitle().equals("개인정보보호처리방침")) {
                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
            } else if (item.getType().equals("FAQ")) {
                Intent intent = new Intent(getActivity(), FaqBodyActivity.class);
                intent.putExtra("title", faqList.get(item.getId()).getTitle());
                intent.putExtra("date", faqList.get(item.getId()).getCreated_at());
                intent.putExtra("content", faqList.get(item.getId()).getContent());
                startActivity(intent);
            } else if (item.getType().equals("Notification")) {
                Intent intent = new Intent(getActivity(), NotificationBodyActivity.class);
                intent.putExtra("content", notificationList.get(item.getId()).getContent());
                if (item.getType().equals("1")) {
                    intent.putExtra("title", "[공지] " + notificationList.get(item.getId()).getName());
                } else if (item.getType().equals("2")) {
                    intent.putExtra("title", "[이벤트] " + notificationList.get(item.getId()).getName());
                } else {
                    intent.putExtra("title", notificationList.get(item.getId()).getName());
                }
                intent.putExtra("title", notificationList.get(item.getId()).getName());
                intent.putExtra("date", notificationList.get(item.getId()).getCreated_at());
                startActivity(intent);
            }
            binding.searchEditText.clearFocus();
            binding.searchEditText.setText("");
            binding.recyclerView.setVisibility(View.GONE);
            binding.closeBtn.setVisibility(View.GONE);
            binding.noItem.setVisibility(View.GONE);

        });
        binding.recyclerView.setAdapter(adapter);

//        binding.searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus) {
//                binding.recyclerView.setVisibility(View.VISIBLE);
//                binding.closeBtn.setVisibility(View.VISIBLE);
//                if (filteredSearchSectionModels != null && filteredSearchSectionModels.size() == 0) {
//                    binding.noItem.setVisibility(View.VISIBLE);
//                }
//            } else {
//                binding.recyclerView.setVisibility(View.GONE);
//                binding.closeBtn.setVisibility(View.GONE);
//                binding.noItem.setVisibility(View.GONE);
//            }
//        });

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filteredSearchSectionModels = new ArrayList<>();
                for (int i = 0; i < searchSectionModels.size(); i++) {
                    if (searchSectionModels.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredSearchSectionModels.add(searchSectionModels.get(i));
                    }
                }
                adapter.setItems(filteredSearchSectionModels);
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.closeBtn.setVisibility(View.VISIBLE);
                if (filteredSearchSectionModels.size() == 0) {
                    binding.noItem.setVisibility(View.VISIBLE);
                } else if (filteredSearchSectionModels.size() > 0) {
                    binding.noItem.setVisibility(View.GONE);
                }

                if (binding.searchEditText.getText().toString().equals("")) {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.closeBtn.setVisibility(View.GONE);
                    binding.noItem.setVisibility(View.GONE);
                }
            }
        });

        binding.notificationTitles.setTexts(notificationTitles);
        binding.notificationTitles.setOnClickListener(v -> {

            for (int i = 0; i < notificationList.size(); i++) {
                if (notificationList.get(i).getName().equals(binding.notificationTitles.getText().toString())) {
                    Intent intent = new Intent(getActivity(), NotificationBodyActivity.class);

                    intent.putExtra("content", notificationList.get(i).getContent());
                    intent.putExtra("title", notificationList.get(i).getName());
                    intent.putExtra("date", notificationList.get(i).getCreated_at());
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
        checkBalance();
        binding.progressBar.getProgress();

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
                    if (balance.toString().equals("0")) {
                        binding.checkBalanceLayout.setVisibility(View.GONE);
                        binding.searchEditText.setVisibility(View.VISIBLE);
                    }
                }, error -> {
                    binding.progressBar.setVisibility(View.GONE);
                    // Toast.makeText(this.getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println(error.getMessage());
                    binding.checkBalanceLayout.setVisibility(View.GONE);
                    binding.searchEditText.setVisibility(View.VISIBLE);
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

                searchSectionModels.add(new SearchSectionModel("Notification", title, i));

                notificationList.add(new NotificationModel.Data(model.getData().get(i).getId(),
                        title,
                        model.getData().get(i).getType(),
                        model.getData().get(i).getContent(),
                        model.getData().get(i).getUpdated_at(),
                        model.getData().get(i).getCreated_at()));

                fadingTitles[i] = title;

            }
            adapter.setItems(searchSectionModels);
            binding.notificationTitles.setTexts(fadingTitles);
        }


    }
    public void onError(String error) {


    }

    public void faqItems(FaqModel model) {

        if (model.getCode() == 200) {
            faqList = model.getData();
            for (int i = 0; i < model.getData().size(); i++) {
                searchSectionModels.add(new SearchSectionModel("FAQ", model.getData().get(i).getTitle(), i));
            }
            adapter.setItems(searchSectionModels);
        }


    }
    public void onFaqError(String error) {


    }

}
