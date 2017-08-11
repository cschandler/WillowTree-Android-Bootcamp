package com.chandler.charles.testerino;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by charleschandler on 8/9/17.
 */

public interface WeatherAPI {
    @GET("data/2.5/weather?units=imperial")
    Call<CurrentWeather> getWeather(@Query("zip") String zipAndCountryCode, @Query("APPID") String apiKey);

    @GET("data/2.5/forecast?units=imperial")
    Call<Forecast> getForecast(@Query("zip") String zipAndCountryCode, @Query("APPID") String apiKey);
}
