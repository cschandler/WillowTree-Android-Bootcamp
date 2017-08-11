package com.chandler.charles.testerino;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.transition.Transition;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter dataAdapter;
    private ArrayList<ForecastViewModel> data;
    private Retrofit retrofit;

    private static final String EXTRA_ZIPCODE = "extra_zipcpde";
    private static final String BASE_URL = "https://api.openweathermap.org/";

    public static Intent build(Context context, String zipCode) {
        return new Intent(context, ForecastActivity.class)
                .putExtra(EXTRA_ZIPCODE, zipCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        data = new ArrayList<>();

        callAPI(getIntent().getStringExtra(EXTRA_ZIPCODE));

        setContentView(R.layout.activity_forecast);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new ListAdapter(this.data);
        dataAdapter.setContext(this);
        recyclerView.setAdapter(dataAdapter);
    }

    private void callAPI(String zipCode) {
        WeatherAPI apiService = retrofit.create(WeatherAPI.class);
        String param = zipCode + ",us";

        Log.d("forecast", "Params: " + param);

        Call<Forecast> call = apiService.getForecast(param, getResources().getString(R.string.api_key));
        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if (response.isSuccessful()) {
                    Log.d("forecast", "success");

                    Forecast forecast = response.body();

                    for (com.chandler.charles.testerino.List day : forecast.getList()) {
                        Weather weather = day.getWeather().get(0);
                        Main main = day.getMain();
                        ForecastViewModel viewModel = new ForecastViewModel(weather, main);
                        Log.d("forecast", "created view model");
                        data.add(viewModel);
                    }

                    dataAdapter.updateData(data);

                } else {
                    Log.d("forecast", "external failure");
                    Log.d("forecast", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                Log.d("forecast", "internal failure");
            }
        });
    }

}
