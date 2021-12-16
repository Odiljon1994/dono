package com.centerprime.ttap.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.centerprime.ttap.api.EtherscanAPI;
import com.centerprime.ttap.ui.viewmodel.CoinMarketCapVM;
import com.centerprime.ttap.ui.viewmodel.CurrentFeeVM;
import com.centerprime.ttap.ui.viewmodel.DirectQuestionVM;
import com.centerprime.ttap.ui.viewmodel.EtherScanVM;
import com.centerprime.ttap.ui.viewmodel.EthereumVM;
import com.centerprime.ttap.ui.viewmodel.NotificationVM;
import com.centerprime.ttap.ui.viewmodel.PostWalletAddressVM;

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

//    @Binds
//    @IntoMap
//    @ViewModelKey(CoinMarketCapVM.class)
//    abstract ViewModel bindCoinMarketCapVM(CoinMarketCapVM coinMarketCapVM);

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

}
