package com.centerprime.ttap.api;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class ApiUtils {

    private static boolean isMainnet = false;

    private static final String etherScan = "https://api.etherscan.io/api/"; // for mainnet
    private static final String baseUrl = "http://3.34.99.232:3001";

    public static String getBaseUrl() {return baseUrl;}

    public static String getEtherscanUrl() {
        if (isMainnet) {
            return etherScan;
        } else {
            return "https://api-ropsten.etherscan.io/api/";
        }
    }

    public static String getInfura() {
        if (isMainnet) {
            return "https://mainnet.infura.io/v3/7c36e7f5656d4384bbcb2cbaf67ad699";
        } else {
            return "https://ropsten.infura.io/v3/ebcfe32ac5ca498d87f8d8b8dce1567a";
        }
    }


    public static String getContractAddress() {
        if (isMainnet) {
            return "0x34752c974c3afb37a4eb7d8489c4d8e4117e92ff";
        } else {
           // return "0x110a13FC3efE6A245B50102D2d79B3E76125Ae83";
            return "0xe021ef6f5a6c18bac762975b915574c6128a31ea";
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
