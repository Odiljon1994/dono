package com.cicenterprise.wellet.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cicenterprise.wellet.ui.viewmodel.BannersListVM;
import com.cicenterprise.wellet.ui.viewmodel.CoinMarketCapVM;
import com.cicenterprise.wellet.ui.viewmodel.CurrentFeeVM;
import com.cicenterprise.wellet.ui.viewmodel.DirectQuestionVM;
import com.cicenterprise.wellet.ui.viewmodel.EtherScanVM;
import com.cicenterprise.wellet.ui.viewmodel.EthereumVM;
import com.cicenterprise.wellet.ui.viewmodel.FaqVM;
import com.cicenterprise.wellet.ui.viewmodel.NotificationVM;
import com.cicenterprise.wellet.ui.viewmodel.PostTransactionVM;
import com.cicenterprise.wellet.ui.viewmodel.PostWalletAddressVM;
import com.cicenterprise.wellet.ui.viewmodel.PrivacyPolicyVM;
import com.cicenterprise.wellet.ui.viewmodel.TermsOfUseVM;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(EthereumVM.class)
    abstract ViewModel bindAuthViewModel(EthereumVM authVM);

    @Binds
    @IntoMap
    @ViewModelKey(CurrentFeeVM.class)
    abstract ViewModel bindCurrentFeeVm(CurrentFeeVM currentFeeVM);

    @Binds
    @IntoMap
    @ViewModelKey(EtherScanVM.class)
    abstract ViewModel bindEtherScanVM(EtherScanVM etherScanVM);

    @Binds
    @IntoMap
    @ViewModelKey(PostWalletAddressVM.class)
    abstract ViewModel bindPostWalletAddressVM(PostWalletAddressVM postWalletAddressVM);

    @Binds
    @IntoMap
    @ViewModelKey(DirectQuestionVM.class)
    abstract ViewModel bindDirectQuestionVM(DirectQuestionVM directQuestionVM);

    @Binds
    @IntoMap
    @ViewModelKey(NotificationVM.class)
    abstract ViewModel bindNotificationVM(NotificationVM notificationVM);

    @Binds
    @IntoMap
    @ViewModelKey(PostTransactionVM.class)
    abstract ViewModel bindPostTransactionVM(PostTransactionVM postTransactionVM);

    @Binds
    @IntoMap
    @ViewModelKey(CoinMarketCapVM.class)
    abstract ViewModel bindCoinMarketCapVM(CoinMarketCapVM coinMarketCapVM);

    @Binds
    @IntoMap
    @ViewModelKey(FaqVM.class)
    abstract ViewModel bindFaqVM(FaqVM faqVM);

    @Binds
    @IntoMap
    @ViewModelKey(PrivacyPolicyVM.class)
    abstract ViewModel bindPrivacyPolicy(PrivacyPolicyVM privacyPolicyVM);

    @Binds
    @IntoMap
    @ViewModelKey(TermsOfUseVM.class)
    abstract ViewModel bindTermsOfUse(TermsOfUseVM termsOfUseVM);

    @Binds
    @IntoMap
    @ViewModelKey(BannersListVM.class)
    abstract ViewModel bindBannerList(BannersListVM bannersListVM);

}
