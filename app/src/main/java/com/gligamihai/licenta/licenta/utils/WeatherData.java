package com.gligamihai.licenta.licenta.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherData {

    @SerializedName("visibility")
    public int visibility;

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    @SerializedName("main")
    private Weather weather;

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @SerializedName("wind")
    private Wind wind;

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @SerializedName("weather")
    private List<Main> weatherList;

    public List<Main> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Main> weatherList) {
        this.weatherList = weatherList;
    }
//    @SerializedName("main")
//    private Main main;
//    @SerializedName("wind")
//    private Wind wind;
//    @SerializedName("weather")
//    private List<Weather> weatherList;
//
//    public Main getMain() {
//        return main;
//    }
//
//    public void setMain(Main main) {
//        this.main = main;
//    }
//
//    public Wind getWind() {
//        return wind;
//    }
//
//    public void setWind(Wind wind) {
//        this.wind = wind;
//    }
//
//    public List<Weather> getWeatherList() {
//        return weatherList;
//    }
//
//    public void setWeatherList(List<Weather> weatherList) {
//        this.weatherList = weatherList;
//    }
}
