package com.centerprime.ttap.di;

import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.api.EtherscanAPI;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {
    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }



    @Provides
    @Singleton
    @Named("etherScan")
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        return client.build();
    }

    @Provides
    @Singleton
    @Named("etherScan")
    Retrofit provideBaseRetrofit(Gson gson, @Named("etherScan") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiUtils.getBaseServerUrl())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    EtherscanAPI provideBaseApi(@Named("etherScan") Retrofit retrofit) {
        return retrofit.create(EtherscanAPI.class);
    }
}
