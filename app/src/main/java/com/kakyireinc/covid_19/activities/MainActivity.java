package com.kakyireinc.covid_19.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kakyireinc.covid_19.BuildConfig;
import com.kakyireinc.covid_19.R;
import com.kakyireinc.covid_19.fragments.NationsFragment;
import com.kakyireinc.covid_19.fragments.WorldFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    TextView version, facebook, instagram;
    BottomNavigationView bottomNavigationView;
    BottomSheetBehavior sheetBehavior;
    View linearlayout;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_id));
        interstitialAd = new InterstitialAd(getApplicationContext());
        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        //initializing views
        linearlayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(linearlayout);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        instagram = findViewById(R.id.instagram_follow);
        facebook = findViewById(R.id.facebook_follow);
        version = findViewById(R.id.app_version);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        ChangeBottom(new NationsFragment());


        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String versionNumber = "Version: " + BuildConfig.VERSION_NAME;
        version.setText(versionNumber);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSocial(getString(R.string.facebook));
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSocial(getString(R.string.instagram));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.bottom_nations:
                ChangeBottom(new NationsFragment());
                return true;
            case R.id.bottom_world:
                ChangeBottom(new WorldFragment());
                return true;
        }
        return false;
    }


    private void ChangeBottom(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_info:
                behavior();
                break;
        }
        return true;
    }

    //checking bottom sheet behavior
    private void behavior() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    //method for loading social media
    private void loadSocial(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

}
