package com.gligamihai.licenta.licenta.utils;

import com.gligamihai.licenta.BuildConfig;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    //@GET("weather?APPID=fb8606bc724b6e2f0a2144ed27f7bbf4&units=metric")
    @GET("weather?APPID="+ BuildConfig.WEATHER_API_KEY+"&units=metric")
    Call<WeatherData> getWeatherData(@Query("q") String cityName);
}
