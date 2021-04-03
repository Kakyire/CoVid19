package com.kakyireinc.covid19.utils;

import android.os.Handler;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class OffRefresh {

    public void offRefresh(final SwipeRefreshLayout swipeRefreshLayout){
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }


}
