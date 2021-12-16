package com.centerprime.ttap.di;

import com.centerprime.ttap.api.Api;
import com.centerprime.ttap.api.ApiUtils;
import com.centerprime.ttap.api.CoinMarketCapAPI;
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
