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

    public static boolean isMainnet = true;

    private static final String etherScan = "https://api.polygonscan.com/api/"; // for mainnet
    // private static final String baseUrl = "http://3.34.99.232:3001";
    private static final String baseUrl = "http://54.193.8.247:3001";

    private static final String coinMarketCap = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/";

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getCoinMarketCap() {
        return coinMarketCap;
    }

    public static String getEtherscanUrl() {
        if (isMainnet) {
            return etherScan;
        } else {
            return "https://api-testnet.polygonscan.com/api/";
        }
    }

    public static String getInfura() {
        if (isMainnet) {
            return "https://polygon-mainnet.infura.io/v3/a396c3461ac048a59f389c7778f06689";
        } else {
            return "https://polygon-amoy.infura.io/v3/a396c3461ac048a59f389c7778f06689";
        }
    }


    public static String getContractAddress() {
        if (isMainnet) {
            return "0x6f8a06447Ff6FcF75d803135a7de15CE88C1d4ec";
        } else {
            return "0x457EdF86dfd45a165FB72910F537DbE0D30a4aad";
        }
    }


    public static String getBnbContractAddress() {
        if (isMainnet) {
            return "0xB8c77482e45F1F44dE1745F52C74426C631bDD52";
        } else {

            return "0x625e7c977cebab5734316574a6d0dc9e53ac0057";
        }
    }

    public static String getGoldERC1155ContractAddress() {
        if (isMainnet) {
            return "0xcf4433ee412bb5d5bf82532ad3fc68418c45d242";
        } else {
            return "";
        }
    }

    public static String getSilverERC1155ContractAddress() {
        if (isMainnet) {
            return "0xc8d4a8265Ba93800Ac7Bc937762510fcA504E846";
        } else {
            return "";
        }
    }

    public static String getBronzeERC1155ContractAddress() {
        if (isMainnet) {
            return "0x8fa438a66737Ea9adD836565CCf0bd262dE5FbFd";
        } else {
            return "";
        }
    }

    public static String getOperatorPrivateKeyAddress() {
        if (isMainnet) {
            return "0xfa6f3d2d337df1930927450e2b821e567d5e6bc4515fb9279f80186a78aff054";
        } else {
            return "";
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
