package com.kakyireinc.covid19.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.kakyireinc.covid19.R;
import com.kakyireinc.covid19.adapters.RecylerAdapter;
import com.kakyireinc.covid19.interfaces.RetrofitClient;
import com.kakyireinc.covid19.models.NationsCases;
import com.kakyireinc.covid19.utils.OffRefresh;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NationsFragment extends Fragment {


    //    view
    TextView network;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    SearchView searchView;
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

        network=view.findViewById(R.id.network_problem);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.nation_swipe);
        recyclerView = view.findViewById(R.id.nations_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecylerAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        list.clear();
        adapter.notifyDataSetChanged();
        LoadAds();
        LoadItems();

        setHasOptionsMenu(true);//enabling option menu
        swipeRefreshLayout.setOnRefreshListener(() -> {
            network.setVisibility(View.GONE);
            list.clear();
            adapter.notifyDataSetChanged();
            LoadAds();
            LoadItems();
        });

        return view;
    }

    private void LoadItems() {

        RetrofitClient.GetData nations = RetrofitClient.getRetrofitInstance().create(RetrofitClient.GetData.class);
        Call<List<NationsCases>> call = nations.getNationCases();
        call.enqueue(new Callback<List<NationsCases>>() {
            @Override
            public void onResponse(Call<List<NationsCases>> call, Response<List<NationsCases>> response) {

                List<NationsCases> mNations = response.body();
                list.addAll(mNations);
                adapter.notifyDataSetChanged();
                new OffRefresh().offRefresh(swipeRefreshLayout);
                progressBar.setVisibility(View.INVISIBLE);
                network.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<List<NationsCases>> call, Throwable t) {
                Log.i("LOG", t.getMessage());
                new OffRefresh().offRefresh(swipeRefreshLayout);
                progressBar.setVisibility(View.INVISIBLE);
                network.setVisibility(View.VISIBLE);
            }
        });


    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.nation_search);
        searchView = (SearchView) item.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Country name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void LoadAds() {


        AdLoader.Builder builder = new AdLoader.Builder(Objects.requireNonNull(getActivity()), getString(R.string.native_ad));

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

        if (nativeAds.size() <= 0) {
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
