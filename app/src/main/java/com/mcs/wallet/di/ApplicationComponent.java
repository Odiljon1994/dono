package com.mcs.wallet.di;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.ui.AddTokenActivity;
import com.mcs.wallet.ui.AddressesBookActivity;
import com.mcs.wallet.ui.BackUpQrCodeActivity;
import com.mcs.wallet.ui.BackUpSeedsActivity;
import com.mcs.wallet.ui.DirectQuestionActivity;
import com.mcs.wallet.ui.ExportPrivatekeyActivity;
import com.mcs.wallet.ui.FaqActivity;
import com.mcs.wallet.ui.ImportWalletActivity;
import com.mcs.wallet.ui.ImportWalletTypeActivity;
import com.mcs.wallet.ui.LockAppActivity;
import com.mcs.wallet.ui.LockOtpActivity;
import com.mcs.wallet.ui.LoginOTPActivity;
import com.mcs.wallet.ui.MainActivity;
import com.mcs.wallet.ui.NotificationActivity;
import com.mcs.wallet.ui.OtpActivity;
import com.mcs.wallet.ui.OtpExportKeysActivity;
import com.mcs.wallet.ui.OtpImportWalletActivity;
import com.mcs.wallet.ui.PrivacyPolicyActivity;
import com.mcs.wallet.ui.ReceiveActivity;
import com.mcs.wallet.ui.SaveQrCodeActivity;
import com.mcs.wallet.ui.ScanQrCodeActivity;
import com.mcs.wallet.ui.SendActivity;
import com.mcs.wallet.ui.SendOtpActivity;
import com.mcs.wallet.ui.SplashActivity;
import com.mcs.wallet.ui.TermsOfUseActivity;
import com.mcs.wallet.ui.VerifySendingActivity;
import com.mcs.wallet.ui.WalletActivity;
import com.mcs.wallet.ui.WalletCreatedActivity;
import com.mcs.wallet.ui.WalletImportedActivity;
import com.mcs.wallet.ui.WalletSeedsActivity;
import com.mcs.wallet.ui.fragments.MainFragment;
import com.mcs.wallet.ui.fragments.MainFragment2;
import com.mcs.wallet.ui.fragments.MainFragment3;
import com.mcs.wallet.ui.fragments.MainFragment4;
import com.mcs.wallet.ui.fragments.MainFragment5;
import com.mcs.wallet.ui.fragments.SettingsFragment;
import com.mcs.wallet.ui.fragments.TokenFragment;
import com.mcs.wallet.ui.fragments.TokensFragment;
import com.mcs.wallet.ui.fragments.WalletFragment;
import com.mcs.wallet.ui.fragments.WalletFragment2;
import com.mcs.wallet.ui.TokenActivity;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        NetModule.class,
        ViewModelModule.class})
public interface ApplicationComponent {
    void inject(MyApp activity);
    void inject(OtpActivity activity);
    void inject(WalletSeedsActivity activity);
    void inject(SplashActivity activity);
    void inject(ReceiveActivity activity);
    void inject(SendActivity activity);
    void inject(TokenFragment fragment);
    void inject(MainFragment fragment);
    void inject(MainFragment2 fragment);
    void inject(WalletFragment fragment);
    void inject(VerifySendingActivity fragment);
    void inject(SendOtpActivity fragment);
    void inject(SaveQrCodeActivity activity);
    void inject(OtpExportKeysActivity activity);
    void inject(BackUpQrCodeActivity activity);
    void inject(BackUpSeedsActivity activity);
    void inject(ExportPrivatekeyActivity activity);
    void inject(MainActivity activity);
    void inject(ImportWalletActivity activity);
    void inject(OtpImportWalletActivity activity);
    void inject(ScanQrCodeActivity activity);
    void inject(WalletImportedActivity activity);
    void inject(WalletCreatedActivity activity);
    void inject(DirectQuestionActivity activity);
    void inject(NotificationActivity activity);
    void inject(LockAppActivity activity);
    void inject(LoginOTPActivity activity);
    void inject(LockOtpActivity activity);
    void inject(TokensFragment fragment);
    void inject(FaqActivity activity);
    void inject(PrivacyPolicyActivity activity);
    void inject(TermsOfUseActivity activity);
    void inject(MainFragment3 fragment3);
    void inject(MainFragment4 fragment4);
    void inject(MainFragment5 fragment5);
    void inject(AddTokenActivity activity);
    void inject(WalletFragment2 fragment2);
    void inject(SettingsFragment fragment2);
    void inject(AddressesBookActivity activity);
    void inject(TokenActivity activity);
    void inject(WalletActivity activity);
    void inject(ImportWalletTypeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
