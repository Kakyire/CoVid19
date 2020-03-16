package com.kakyireinc.covid_19.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kakyireinc.covid_19.R;
import com.kakyireinc.covid_19.adapters.RecylerAdapter;
import com.kakyireinc.covid_19.interfaces.RetrofitClient;
import com.kakyireinc.covid_19.models.WorldCases;

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

public class WorldFragment extends Fragment {


    View view;
    RecyclerView recyclerView;
    RecylerAdapter adapter;
    List<Object> list = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.world_fragment, container, false);

        recyclerView = view.findViewById(R.id.world_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RecylerAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        LoadItems();


        return view;
    }


    private void settingValues() {

    }

    private void LoadItems() {
        final RetrofitClient.GetData world = RetrofitClient.getRetrofitInstance().create(RetrofitClient.GetData.class);
        Call<List<WorldCases>> call = world.getWorldCases();
        call.enqueue(new Callback<List<WorldCases>>() {
            @Override
            public void onResponse(Call<List<WorldCases>> call, Response<List<WorldCases>> response) {

                List<WorldCases> worldCases = response.body();
                list.add(worldCases);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<WorldCases>> call, Throwable t) {
                Log.i("Error", t.getMessage());

            }
        });
    }

}
