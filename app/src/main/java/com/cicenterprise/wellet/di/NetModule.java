package com.cicenterprise.wellet.di;

import com.cicenterprise.wellet.api.Api;
import com.cicenterprise.wellet.api.ApiUtils;
import com.cicenterprise.wellet.api.CoinMarketCapAPI;
import com.cicenterprise.wellet.api.EtherscanAPI;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

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


    // EtherScan API

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
                .baseUrl(ApiUtils.getEtherscanUrl())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    EtherscanAPI provideEtherScanApi(@Named("etherScan") Retrofit retrofit) {
        return retrofit.create(EtherscanAPI.class);
    }



    // BaseServer API

    @Provides
    @Singleton
    @Named("baseAPI")
    OkHttpClient provideOkHttpClient2() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        return client.build();
    }

    @Provides
    @Singleton
    @Named("baseAPI")
    Retrofit provideBaseRetrofit2(Gson gson, @Named("baseAPI") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiUtils.getBaseUrl())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    Api provideBaseApi(@Named("baseAPI") Retrofit retrofit) {
        return retrofit.create(Api.class);
    }


    // CoinMarketCap API

    @Provides
    @Singleton
    @Named("coinMarketCapApi")
    OkHttpClient provideOkHttpClient3() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(60, TimeUnit.SECONDS);
        client.readTimeout(60, TimeUnit.SECONDS);
        return client.build();
    }

    @Provides
    @Singleton
    @Named("coinMarketCapApi")
    Retrofit provideBaseRetrofit3(Gson gson, @Named("coinMarketCapApi") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(ApiUtils.getCoinMarketCap())
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    CoinMarketCapAPI provideCoinMarketCapApi(@Named("coinMarketCapApi") Retrofit retrofit) {
        return retrofit.create(CoinMarketCapAPI.class);
    }
}
