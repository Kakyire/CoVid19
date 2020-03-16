package com.kakyireinc.covid_19.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.kakyireinc.covid_19.R;
import com.kakyireinc.covid_19.adapters.RecylerAdapter;
import com.kakyireinc.covid_19.interfaces.RetrofitClient;
import com.kakyireinc.covid_19.models.NationsCases;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NationsFragment extends Fragment {


    //    view
    RecyclerView recyclerView;
    View view;
    RecylerAdapter adapter;


    private AdLoader adLoader;
    private final int NUMBER_OF_ADS = 5;

    List<Object> list = new ArrayList<>();
    List<UnifiedNativeAd> nativeAds = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.nations_fragment, container, false);

        recyclerView = view.findViewById(R.id.nations_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecylerAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);
        list.clear();
        LoadAds();
        LoadItems();


        return view;
    }

    private void LoadItems() {

        RetrofitClient.GetData nations = RetrofitClient.getRetrofitInstance().create(RetrofitClient.GetData.class);
        Call<List<NationsCases>> call = nations.getNationCases();
        call.enqueue(new Callback<List<NationsCases>>() {
            @Override
            public void onResponse(Call<List<NationsCases>> call, Response<List<NationsCases>> response) {

                List<NationsCases> mNations = response.body();
                list.add(mNations);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<NationsCases>> call, Throwable t) {
                Log.i("LOG", t.getMessage());
            }
        });


    }


    private void LoadAds() {

        AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.sample_nativead));
        adLoader = builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                nativeAds.add(unifiedNativeAd);

                if (!adLoader.isLoading()) {
                    PopulateAds();
                }


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


                //whenever nativeAd index is less than list size ad
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
