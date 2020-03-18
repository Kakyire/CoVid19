package com.kakyireinc.covid_19.utils;

import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class OffRefresh {

    public void offRefresh(final SwipeRefreshLayout swipeRefreshLayout){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }, 1000);
    }


    public void RecycleScroll(RecyclerView recyclerView, boolean scroll){

    }
}
