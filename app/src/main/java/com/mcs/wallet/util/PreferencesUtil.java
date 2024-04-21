package com.mcs.wallet.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String OTP = "OTP";
    private static final String WALLET_ADDRESS = "WALLET_ADDRESS";
    private static final String MNEMONIC = "MNEMONIC";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";
    private static final String IS_REGISTERED = "IS_REGISTERED";
    private static final String IS_APP_LOCKED = "IS_APP_LOCKED";
    private static final String LANGUAGE = "LANGUAGE";



    public PreferencesUtil(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }
    public String getLANGUAGE() {
        return sharedPreferences.getString(LANGUAGE, "");
    }

    public void saveLanguage(String language) {
        sharedPreferences.edit().putString(LANGUAGE, language).apply();
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

    public void savePrivateKey(String privateKey) {
        sharedPreferences.edit().putString(PRIVATE_KEY, privateKey).apply();
    }
    public String getPrivateKey() {
        return sharedPreferences.getString(PRIVATE_KEY, "");
    }

    public void saveMnemonic(String mnemonic) {
        sharedPreferences.edit().putString(MNEMONIC, mnemonic).apply();
    }
    public String getMnemonic() {
        return sharedPreferences.getString(MNEMONIC, "");
    }

    public void saveIsRegistered(boolean isRegistered) {
        sharedPreferences.edit().putBoolean(IS_REGISTERED, isRegistered).apply();
    }
    public boolean getIsRegistered() {
        return sharedPreferences.getBoolean(IS_REGISTERED, false);
    }

    public void saveIsAppLocked(boolean isAppLocked) {
        sharedPreferences.edit().putBoolean(IS_APP_LOCKED, isAppLocked).apply();
    }
    public boolean getIsAppLocked() {
        return sharedPreferences.getBoolean(IS_APP_LOCKED, false);
    }



}
