package com.centerprime.ttap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.centerprime.ttap.MyApp;
import com.centerprime.ttap.R;
import com.centerprime.ttap.databinding.ActivityMainBinding;
import com.centerprime.ttap.ui.fragments.AddressesBookFragment;
import com.centerprime.ttap.ui.fragments.MainFragment;
import com.centerprime.ttap.ui.fragments.MainFragment2;
import com.centerprime.ttap.ui.fragments.SettingsFragment;
import com.centerprime.ttap.ui.fragments.WalletFragment;
import com.centerprime.ttap.util.PreferencesUtil;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

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

        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.addressBook:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddressesBookFragment()).commit();
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    binding.appBar.setVisibility(View.GONE);
                    break;
                case R.id.directMessage:
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    binding.appBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(MainActivity.this, DirectQuestionActivity.class));
                    break;
                case R.id.notice:
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    binding.appBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                    break;
                case R.id.faq:
                    binding.drawerLayout.closeDrawer(GravityCompat.START);
                    binding.appBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(MainActivity.this, FaqActivity.class));
                    break;

            }

            return true;

        });

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