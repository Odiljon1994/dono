package com.centerprime.ttap.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String OTP = "OTP";
    private static final String WALLET_ADDRESS = "WALLET_ADDRESS";


    public PreferencesUtil(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }


    public void saveOtp(String otp) {
        sharedPreferences.edit().putString(OTP, otp).apply();
    }
    public String getOtp() {
        return sharedPreferences.getString(OTP, "");
    }

    public void saveWalletAddress(String walletAddress) {
        sharedPreferences.edit().putString(WALLET_ADDRESS, walletAddress).apply();
    }
    public String getWalletAddress() {
        return sharedPreferences.getString(WALLET_ADDRESS, "");
    }



}
