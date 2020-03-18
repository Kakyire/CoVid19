package com.kakyireinc.covid_19.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kakyireinc.covid_19.R;
import com.kakyireinc.covid_19.adapters.RecylerAdapter;
import com.kakyireinc.covid_19.interfaces.RetrofitClient;
import com.kakyireinc.covid_19.models.WorldCases;
import com.kakyireinc.covid_19.utils.OffRefresh;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorldFragment extends Fragment {


    View view;
    TextView network;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    RecylerAdapter adapter;
    List<Object> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.world_fragment, container, false);

        network = view.findViewById(R.id.network_problem);
        progressBar = view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = view.findViewById(R.id.world_swipe);
        recyclerView = view.findViewById(R.id.world_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecylerAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);


        list.clear();
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        LoadItems();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                LoadAds();
                network.setVisibility(View.GONE);
                list.clear();
                adapter.notifyDataSetChanged();
                LoadItems();
            }
        });
        return view;
    }


    private void LoadItems() {
        final RetrofitClient.GetData world = RetrofitClient.getRetrofitInstance().create(RetrofitClient.GetData.class);
        Call<WorldCases> call = world.getWorldCases();
        call.enqueue(new Callback<WorldCases>() {
            @Override
            public void onResponse(Call<WorldCases> call, Response<WorldCases> response) {
                WorldCases worldCases = response.body();
                list.add(worldCases);
                adapter.notifyDataSetChanged();
                new OffRefresh().offRefresh(swipeRefreshLayout);
                progressBar.setVisibility(View.INVISIBLE);
                network.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<WorldCases> call, Throwable t) {
                Log.i("Error", t.getMessage());
                new OffRefresh().offRefresh(swipeRefreshLayout);
                progressBar.setVisibility(View.INVISIBLE);
                network.setVisibility(View.VISIBLE);
            }
        });
    }

}
