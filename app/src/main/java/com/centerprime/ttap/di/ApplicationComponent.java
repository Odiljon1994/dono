package com.centerprime.ttap.di;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.ui.BackUpQrCodeActivity;
import com.centerprime.ttap.ui.BackUpSeedsActivity;
import com.centerprime.ttap.ui.ExportPrivatekeyActivity;
import com.centerprime.ttap.ui.OtpActivity;
import com.centerprime.ttap.ui.OtpExportKeysActivity;
import com.centerprime.ttap.ui.ReceiveActivity;
import com.centerprime.ttap.ui.SaveQrCodeActivity;
import com.centerprime.ttap.ui.SendActivity;
import com.centerprime.ttap.ui.SendOtpActivity;
import com.centerprime.ttap.ui.SplashActivity;
import com.centerprime.ttap.ui.VerifySendingActivity;
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


    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
