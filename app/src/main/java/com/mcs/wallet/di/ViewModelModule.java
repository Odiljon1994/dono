package com.mcs.wallet.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mcs.wallet.ui.viewmodel.BannersListVM;
import com.mcs.wallet.ui.viewmodel.CoinMarketCapVM;
import com.mcs.wallet.ui.viewmodel.CurrentFeeVM;
import com.mcs.wallet.ui.viewmodel.DirectQuestionVM;
import com.mcs.wallet.ui.viewmodel.EtherScanVM;
import com.mcs.wallet.ui.viewmodel.EthereumVM;
import com.mcs.wallet.ui.viewmodel.FaqVM;
import com.mcs.wallet.ui.viewmodel.NotificationVM;
import com.mcs.wallet.ui.viewmodel.PostTransactionVM;
import com.mcs.wallet.ui.viewmodel.PostWalletAddressVM;
import com.mcs.wallet.ui.viewmodel.PrivacyPolicyVM;
import com.mcs.wallet.ui.viewmodel.TermsOfUseVM;

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