package com.mcs.wallet.ui;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mcs.wallet.MyApp;
import com.mcs.wallet.ui.fragments.SettingsFragment;
import com.mcs.wallet.ui.fragments.WalletFragment2;
import com.mcs.wallet.util.PreferencesUtil;
import com.mcs.wallet.R;
import com.mcs.wallet.databinding.ActivityMainBinding;
import com.mcs.wallet.ui.fragments.MainFragment5;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static AppBarLayout appBarLayout;

    private MainFragment5 mainFragment3;
    private WalletFragment2 walletFragment2;
    private SettingsFragment settingsFragment;
    @Inject
    PreferencesUtil preferencesUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setAppLocale(preferencesUtil.getLANGUAGE());

//        EthManager ethManager = EthManager.getInstance();
//        ethManager.init("https://mainnet.infura.io/v3/7c36e7f5656d4384bbcb2cbaf67ad699");
//        ethManager.importFromPrivateKey("0xd9b7b635e587629b59984543276813e9c087a5c1eb43d7f555d07341e0689245", this)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(walletAddress -> {
//                    System.out.println(walletAddress);
//                    //showDialog();
//                    Toast.makeText(this, walletAddress, Toast.LENGTH_SHORT).show();
//
//                });

        mainFragment3 = new MainFragment5();
        walletFragment2 = new WalletFragment2();
        settingsFragment = new SettingsFragment();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_open, R.string.navigation_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment3).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, walletFragment2).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, settingsFragment).commit();


        changeState(1);

        appBarLayout = findViewById(R.id.appBar);
        binding.bottomNavigation.setItemIconTintList(null);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    changeState(1);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment3()).commit();
                    binding.appBar.setVisibility(View.VISIBLE);
                    break;
                case R.id.wallet:
                    changeState(2);
                    //   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment2()).commit();
                    binding.appBar.setVisibility(View.VISIBLE);

                    break;
                case R.id.lock:
                    changeState(3);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                    binding.appBar.setVisibility(View.VISIBLE);
                    break;

            }

            return true;
        });


        View headerView = binding.navView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.clientId);
        navUsername.setText(preferencesUtil.getWalletAddress());

        TextView myPage = headerView.findViewById(R.id.myPage);
        TextView addressBook = headerView.findViewById(R.id.addressBook);
        TextView settings = headerView.findViewById(R.id.settings);
        TextView notice = headerView.findViewById(R.id.notice);
        TextView faq = headerView.findViewById(R.id.faq);
        TextView directMessage = headerView.findViewById(R.id.directMessage);
        TextView terms = headerView.findViewById(R.id.terms);
        TextView privacy = headerView.findViewById(R.id.privacy);
        TextView version = headerView.findViewById(R.id.version);

        CheckBox isKor = headerView.findViewById(R.id.isKor);
        CheckBox isJp = headerView.findViewById(R.id.isJp);

        if (preferencesUtil.getLANGUAGE().equals("ja")) {
            isJp.setChecked(true);

        } else {
            isKor.setChecked(true);
        }

        isJp.setOnClickListener(v -> {
            isKor.setChecked(false);
            isJp.setChecked(true);
            setAppLocale("ja");
            preferencesUtil.saveLanguage("ja");
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            // recreate();
        });

        isKor.setOnClickListener(v -> {
            isJp.setChecked(false);
            isKor.setChecked(true);
            setAppLocale("en");
            preferencesUtil.saveLanguage("en");
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            //   recreate();
        });

        terms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        privacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        myPage.setOnClickListener(v -> {
            binding.appBar.setVisibility(View.VISIBLE);
            binding.bottomNavigation.setSelectedItemId(R.id.wallet);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            changeState(2);
        });
        addressBook.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.appBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, AddressesBookActivity.class));

        });
        settings.setOnClickListener(v -> {
            changeState(3);
            binding.appBar.setVisibility(View.VISIBLE);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.bottomNavigation.setSelectedItemId(R.id.lock);
        });
        notice.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.appBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        });
        faq.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.appBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, FaqActivity.class));
        });
        directMessage.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.appBar.setVisibility(View.VISIBLE);
            startActivity(new Intent(MainActivity.this, DirectQuestionActivity.class));
        });
        terms.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, TermsOfUseActivity.class));
        });
        privacy.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
        });
        version.setOnClickListener(v -> {

        });
    }


    private void changeState(int position) {
        if (position == 1) {
            getSupportFragmentManager().beginTransaction().show(mainFragment3).commit();
            getSupportFragmentManager().beginTransaction().hide(walletFragment2).commit();
            getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();

        } else if (position == 2) {
            getSupportFragmentManager().beginTransaction().hide(mainFragment3).commit();
            getSupportFragmentManager().beginTransaction().show(walletFragment2).commit();
            getSupportFragmentManager().beginTransaction().hide(settingsFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().hide(mainFragment3).commit();
            getSupportFragmentManager().beginTransaction().hide(walletFragment2).commit();
            getSupportFragmentManager().beginTransaction().show(settingsFragment).commit();

        }
    }

    private void setAppLocale(String localCode) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localCode.toLowerCase());
        }
        resources.updateConfiguration(configuration, displayMetrics);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
}