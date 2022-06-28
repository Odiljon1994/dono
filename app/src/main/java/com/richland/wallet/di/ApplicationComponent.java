package com.richland.wallet.di;

import com.richland.wallet.MyApp;
import com.richland.wallet.ui.AddTokenActivity;
import com.richland.wallet.ui.AddressesBookActivity;
import com.richland.wallet.ui.BackUpQrCodeActivity;
import com.richland.wallet.ui.BackUpSeedsActivity;
import com.richland.wallet.ui.DirectQuestionActivity;
import com.richland.wallet.ui.ExportPrivatekeyActivity;
import com.richland.wallet.ui.FaqActivity;
import com.richland.wallet.ui.ImportWalletActivity;
import com.richland.wallet.ui.ImportWalletTypeActivity;
import com.richland.wallet.ui.LockAppActivity;
import com.richland.wallet.ui.LockOtpActivity;
import com.richland.wallet.ui.LoginOTPActivity;
import com.richland.wallet.ui.MainActivity;
import com.richland.wallet.ui.NotificationActivity;
import com.richland.wallet.ui.OtpActivity;
import com.richland.wallet.ui.OtpExportKeysActivity;
import com.richland.wallet.ui.OtpImportWalletActivity;
import com.richland.wallet.ui.PrivacyPolicyActivity;
import com.richland.wallet.ui.ReceiveActivity;
import com.richland.wallet.ui.SaveQrCodeActivity;
import com.richland.wallet.ui.ScanQrCodeActivity;
import com.richland.wallet.ui.SendActivity;
import com.richland.wallet.ui.SendOtpActivity;
import com.richland.wallet.ui.SplashActivity;
import com.richland.wallet.ui.TermsOfUseActivity;
import com.richland.wallet.ui.VerifySendingActivity;
import com.richland.wallet.ui.WalletActivity;
import com.richland.wallet.ui.WalletCreatedActivity;
import com.richland.wallet.ui.WalletImportedActivity;
import com.richland.wallet.ui.WalletSeedsActivity;
import com.richland.wallet.ui.fragments.MainFragment;
import com.richland.wallet.ui.fragments.MainFragment2;
import com.richland.wallet.ui.fragments.MainFragment3;
import com.richland.wallet.ui.fragments.MainFragment4;
import com.richland.wallet.ui.fragments.MainFragment5;
import com.richland.wallet.ui.fragments.SettingsFragment;
import com.richland.wallet.ui.fragments.TokenFragment;
import com.richland.wallet.ui.fragments.TokensFragment;
import com.richland.wallet.ui.fragments.WalletFragment;
import com.richland.wallet.ui.fragments.WalletFragment2;
import com.richland.wallet.ui.TokenActivity;

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
