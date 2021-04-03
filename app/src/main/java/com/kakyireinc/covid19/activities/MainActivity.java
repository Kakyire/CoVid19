package com.kakyireinc.covid19.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.kakyireinc.covid19.BuildConfig;
import com.kakyireinc.covid19.R;
import com.kakyireinc.covid19.fragments.NationsFragment;
import com.kakyireinc.covid19.fragments.WorldFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    TextView version;
    TextView facebook;
    TextView instagram;
    TextView twitter;
    BottomNavigationView bottomNavigationView;
    BottomSheetBehavior sheetBehavior;
    View linearLayout;
    InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        uninstallApp();


        //initializing views
        initializingViews();

        changeFragment(new NationsFragment());




        String versionNumber = "Version: " + BuildConfig.VERSION_NAME;
        version.setText(versionNumber);
        facebook.setOnClickListener(v -> loadSocialMedia(getString(R.string.facebook)));

        instagram.setOnClickListener(v -> loadSocialMedia(getString(R.string.instagram)));

        twitter.setOnClickListener(v -> loadSocialMedia(getString(R.string.twitter)));
    }

    private void initializingViews() {
        linearLayout = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(linearLayout);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        twitter = findViewById(R.id.twitter_follow);
        instagram = findViewById(R.id.instagram_follow);
        facebook = findViewById(R.id.facebook_follow);
        version = findViewById(R.id.app_version);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.bottom_nations) {
            changeFragment(new NationsFragment());
            return true;
        } else if (id == R.id.bottom_world) {
            changeFragment(new WorldFragment());
            return true;
        }
        return false;
    }


    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_info) {
            showHideBottomSheet();
        }
        return true;
    }

    //checking bottom sheet behavior
    private void showHideBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    //method for loading social media
    private void loadSocialMedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }


    Intent uninstallIntent;

    //check if old version is installed
    private void uninstallApp() {
        uninstallIntent = getPackageManager().getLaunchIntentForPackage("com.kakyireinc.covid_19");

        if (uninstallIntent != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Uninstalled Old Version")
                    .setMessage(Html.fromHtml("Old version of <b>COVID-19</b> detected.<br> Please <b>Uninstall</b> "))
                    .setPositiveButton("Uninstall", (dialog, which) -> {
                        Uri packageToRemove = Uri.parse("package:com.kakyireinc.covid_19");
                        uninstallIntent = new Intent(Intent.ACTION_DELETE, packageToRemove);
                        startActivity(uninstallIntent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> finish());
            builder.create()
                    .show();


        }
    }
}
