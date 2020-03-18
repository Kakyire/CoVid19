package com.kakyireinc.covid_19.interfaces;

import com.kakyireinc.covid_19.models.NationsCases;
import com.kakyireinc.covid_19.models.WorldCases;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String COVID_URL = "https://coronavirus-19-api.herokuapp.com/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(COVID_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }


    public interface GetData {
        @GET("/all")
        Call<WorldCases> getWorldCases();

        @GET("/countries")
        Call<List<NationsCases>> getNationCases();
    }
}
