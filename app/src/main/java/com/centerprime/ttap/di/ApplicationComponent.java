package com.centerprime.ttap.di;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.ui.OtpActivity;
import com.centerprime.ttap.ui.ReceiveActivity;
import com.centerprime.ttap.ui.SendActivity;
import com.centerprime.ttap.ui.SplashActivity;
import com.centerprime.ttap.ui.WalletSeedsActivity;
import com.centerprime.ttap.ui.fragments.TokenFragment;

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

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MyApp application);

        Builder appModule(AppModule appModule);

        ApplicationComponent build();
    }
}
