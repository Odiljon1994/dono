package com.centerprime.ttap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.os.BaseBundle;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityMainBinding;
import com.centerprime.ttap.ui.fragments.AddressesBookFragment;
import com.centerprime.ttap.ui.fragments.MainFragment;
import com.centerprime.ttap.ui.fragments.MainFragment2;
import com.centerprime.ttap.ui.fragments.SettingsFragment;
import com.centerprime.ttap.ui.fragments.WalletFragment;
import com.centerprime.ttap.util.PreferencesUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static AppBarLayout appBarLayout;

    @Inject
    PreferencesUtil preferencesUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplication()).getAppComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment2()).commit();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_open, R.string.navigation_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        appBarLayout = findViewById(R.id.appBar);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment2()).commit();
                    binding.appBar.setVisibility(View.VISIBLE);
                    break;
                case R.id.wallet:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
                    binding.appBar.setVisibility(View.VISIBLE);

                    break;
                case R.id.lock:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
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

        terms.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        privacy.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        myPage.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
            binding.appBar.setVisibility(View.VISIBLE);
            binding.bottomNavigation.setSelectedItemId(R.id.wallet);
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        addressBook.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddressesBookFragment()).commit();
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.appBar.setVisibility(View.GONE);
        });
        settings.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
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

//        binding.navView.setNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.addressBook:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddressesBookFragment()).commit();
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
//                    binding.appBar.setVisibility(View.GONE);
//                    break;
//                case R.id.directMessage:
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
//                    binding.appBar.setVisibility(View.VISIBLE);
//                    startActivity(new Intent(MainActivity.this, DirectQuestionActivity.class));
//                    break;
//                case R.id.notice:
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
//                    binding.appBar.setVisibility(View.VISIBLE);
//                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
//                    break;
//                case R.id.faq:
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
//                    binding.appBar.setVisibility(View.VISIBLE);
//                    startActivity(new Intent(MainActivity.this, FaqActivity.class));
//                    break;
//                case R.id.mainPage:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new WalletFragment()).commit();
//                    binding.appBar.setVisibility(View.VISIBLE);
//                    binding.bottomNavigation.setSelectedItemId(R.id.wallet);
//                    binding.drawerLayout.closeDrawer(GravityCompat.START);
//                    break;
//            }
//
//            return true;
//
//        });

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