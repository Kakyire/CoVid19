package com.kakyireinc.covid19.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.kakyireinc.covid19.R;
import com.kakyireinc.covid19.adapters.RecylerAdapter;
import com.kakyireinc.covid19.interfaces.RetrofitClient;
import com.kakyireinc.covid19.models.WorldCases;
import com.kakyireinc.covid19.utils.OffRefresh;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadNationsItems extends AppCompatActivity {


    //    view
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    View view;
    RecylerAdapter adapter;


    private AdLoader adLoader;
    private final int NUMBER_OF_ADS = 5;

    List<Object> list = new ArrayList<>();
    List<UnifiedNativeAd> nativeAds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_nations_items);


//        MobileAds.initialize(this, getString(R.string.sample_admob_id));

//        progressBar = findViewById(R.id.progress_bar);
//        swipeRefreshLayout = findViewById(R.id.nation_swipe);
//        recyclerView = findViewById(R.id.nations_recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new RecylerAdapter(this, list);
//        recyclerView.setAdapter(adapter);
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        list.clear();
//        LoadAds();
//        LoadItems();
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//               list.clear();
//                LoadAds();
//                LoadItems();
//            }
//        });

    }


    private void LoadItems() {

        RetrofitClient.GetData nations = RetrofitClient.getRetrofitInstance().create(RetrofitClient.GetData.class);
        Call<WorldCases> call = nations.getWorldCases();
        call.enqueue(new Callback<WorldCases>() {
            @Override
            public void onResponse(Call<WorldCases> call, Response<WorldCases> response) {

                WorldCases mNations = response.body();
                list.add(mNations);
                adapter.notifyDataSetChanged();
                new OffRefresh().offRefresh(swipeRefreshLayout);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<WorldCases> call, Throwable t) {
                Log.i("LOG", t.getMessage());
                new OffRefresh().offRefresh(swipeRefreshLayout);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }


    private void LoadAds() {


        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.sample_nativead));

        adLoader = builder.forUnifiedNativeAd(unifiedNativeAd -> {
            nativeAds.add(unifiedNativeAd);

            if (!adLoader.isLoading()) {
                    PopulateAds();
            }

        }).withAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                if (!adLoader.isLoading()) {
                    PopulateAds();
                }
            }
        }).build();
        adLoader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);

    }


    private void PopulateAds() {
        if (nativeAds.size() > 0) {
            return;
        }


        int index = 5;//first index of native ad

        do {

            for (UnifiedNativeAd ad : nativeAds) {


                //whenever nativeAd index is less than list size add
                //it to the list
                if (index <= list.size()) {

                    list.add(index, ad);
                }

                index = index + 6;//increase index size by 6
            }
        } while (index < list.size());


        adapter.notifyDataSetChanged();
    }

}
