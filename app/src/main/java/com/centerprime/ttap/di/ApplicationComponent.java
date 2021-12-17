package com.centerprime.ttap.di;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.ui.BackUpQrCodeActivity;
import com.centerprime.ttap.ui.BackUpSeedsActivity;
import com.centerprime.ttap.ui.DirectQuestionActivity;
import com.centerprime.ttap.ui.ExportPrivatekeyActivity;
import com.centerprime.ttap.ui.ImportWalletActivity;
import com.centerprime.ttap.ui.LockAppActivity;
import com.centerprime.ttap.ui.LockOtpActivity;
import com.centerprime.ttap.ui.LoginOTPActivity;
import com.centerprime.ttap.ui.MainActivity;
import com.centerprime.ttap.ui.NotificationActivity;
import com.centerprime.ttap.ui.OtpActivity;
import com.centerprime.ttap.ui.OtpExportKeysActivity;
import com.centerprime.ttap.ui.OtpImportWalletActivity;
import com.centerprime.ttap.ui.ReceiveActivity;
import com.centerprime.ttap.ui.SaveQrCodeActivity;
import com.centerprime.ttap.ui.ScanQrCodeActivity;
import com.centerprime.ttap.ui.SendActivity;
import com.centerprime.ttap.ui.SendOtpActivity;
import com.centerprime.ttap.ui.SplashActivity;
import com.centerprime.ttap.ui.VerifySendingActivity;
import com.centerprime.ttap.ui.WalletCreatedActivity;
import com.centerprime.ttap.ui.WalletImportedActivity;
import com.centerprime.ttap.ui.WalletSeedsActivity;
import com.centerprime.ttap.ui.fragments.MainFragment;
import com.centerprime.ttap.ui.fragments.MainFragment2;
import com.centerprime.ttap.ui.fragments.TokenFragment;
import com.centerprime.ttap.ui.fragments.WalletFragment;

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


    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
