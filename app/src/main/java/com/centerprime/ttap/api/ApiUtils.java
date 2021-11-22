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

   // private static final String etherScan = "https://api.etherscan.io/api/"; // for mainnet
    private static final String etherScan = "https://api-ropsten.etherscan.io/api/"; // for ropsten testnet



    public static String getBaseServerUrl() {
        return etherScan;
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
