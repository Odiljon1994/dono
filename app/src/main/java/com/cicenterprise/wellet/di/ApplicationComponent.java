package com.cicenterprise.wellet.di;

import com.cicenterprise.wellet.MyApp;
import com.cicenterprise.wellet.ui.AddTokenActivity;
import com.cicenterprise.wellet.ui.AddressesBookActivity;
import com.cicenterprise.wellet.ui.BackUpQrCodeActivity;
import com.cicenterprise.wellet.ui.BackUpSeedsActivity;
import com.cicenterprise.wellet.ui.DirectQuestionActivity;
import com.cicenterprise.wellet.ui.ExportPrivatekeyActivity;
import com.cicenterprise.wellet.ui.FaqActivity;
import com.cicenterprise.wellet.ui.ImportWalletActivity;
import com.cicenterprise.wellet.ui.LockAppActivity;
import com.cicenterprise.wellet.ui.LockOtpActivity;
import com.cicenterprise.wellet.ui.LoginOTPActivity;
import com.cicenterprise.wellet.ui.MainActivity;
import com.cicenterprise.wellet.ui.NotificationActivity;
import com.cicenterprise.wellet.ui.OtpActivity;
import com.cicenterprise.wellet.ui.OtpExportKeysActivity;
import com.cicenterprise.wellet.ui.OtpImportWalletActivity;
import com.cicenterprise.wellet.ui.PrivacyPolicyActivity;
import com.cicenterprise.wellet.ui.ReceiveActivity;
import com.cicenterprise.wellet.ui.SaveQrCodeActivity;
import com.cicenterprise.wellet.ui.ScanQrCodeActivity;
import com.cicenterprise.wellet.ui.SendActivity;
import com.cicenterprise.wellet.ui.SendOtpActivity;
import com.cicenterprise.wellet.ui.SplashActivity;
import com.cicenterprise.wellet.ui.TermsOfUseActivity;
import com.cicenterprise.wellet.ui.VerifySendingActivity;
import com.cicenterprise.wellet.ui.WalletCreatedActivity;
import com.cicenterprise.wellet.ui.WalletImportedActivity;
import com.cicenterprise.wellet.ui.WalletSeedsActivity;
import com.cicenterprise.wellet.ui.fragments.MainFragment;
import com.cicenterprise.wellet.ui.fragments.MainFragment2;
import com.cicenterprise.wellet.ui.fragments.MainFragment3;
import com.cicenterprise.wellet.ui.fragments.MainFragment4;
import com.cicenterprise.wellet.ui.fragments.TokenFragment;
import com.cicenterprise.wellet.ui.fragments.TokensFragment;
import com.cicenterprise.wellet.ui.fragments.WalletFragment;
import com.cicenterprise.wellet.ui.fragments.WalletFragment2;
import com.cicenterprise.wellet.ui.TokenActivity;

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
    void inject(AddTokenActivity activity);
    void inject(WalletFragment2 fragment2);
    void inject(AddressesBookActivity activity);
    void inject(TokenActivity activity);


    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
