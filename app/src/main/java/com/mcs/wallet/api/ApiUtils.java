package com.mcs.wallet.api;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ApiUtils {

    private static boolean isMainnet = true;

    private static final String etherScan = "https://api.etherscan.io/api/"; // for mainnet
   // private static final String baseUrl = "http://3.34.99.232:3001";
    private static final String baseUrl = "http://54.193.8.247:3001";

    private static final String coinMarketCap = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/";

    public static String getBaseUrl() {return baseUrl;}
    public static String getCoinMarketCap() {return coinMarketCap;}

    public static String getEtherscanUrl() {
        if (isMainnet) {
            return etherScan;
        } else {
            return "https://api-holesky.etherscan.io/api/";
        }
    }

    public static String getInfura() {
       // return "http://13.209.112.0:8545";
        if (isMainnet) {
            return "https://mainnet.infura.io/v3/a396c3461ac048a59f389c7778f06689";
        } else {
            return "https://sepolia.infura.io/v3/a396c3461ac048a59f389c7778f06689";
        }


//        if (isMainnet) {
//            return "https://polygon-mainnet.infura.io/v3/a396c3461ac048a59f389c7778f06689";
//        } else {
//            return "https://polygon-amoy.infura.io/v3/a396c3461ac048a59f389c7778f06689";
//        }
    }


    public static String getContractAddress() {
        if (isMainnet) {


            return "0xdac17f958d2ee523a2206206994597c13d831ec7";
        } else {
           // return "0x110a13FC3efE6A245B50102D2d79B3E76125Ae83";
            return "0xe021ef6f5a6c18bac762975b915574c6128a31ea";
        }
    }


    public static String getBnbContractAddress() {
        if (isMainnet) {
            return "0xB8c77482e45F1F44dE1745F52C74426C631bDD52";
        } else {

            return "0x625e7c977cebab5734316574a6d0dc9e53ac0057";
        }
    }

    public static OkHttpClient.Builder configureClient(final OkHttpClient.Builder builder) {

        final TrustManager[] certs = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        try {
            SSLContext ctx;
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, certs, new SecureRandom());  // 인증서 유효성 검증안함
            builder.sslSocketFactory(ctx.getSocketFactory(), (X509TrustManager) certs[0]);
            builder.hostnameVerifier((hostname, session) -> true); // hostname 검증안함
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return builder;
    }
}
