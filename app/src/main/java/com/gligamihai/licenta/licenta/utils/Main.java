package com.gligamihai.licenta.licenta.utils;

import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("id")
    public int weatherId;

    @SerializedName("main")
    public String weatherMain;

    @SerializedName("icon")
    public String weatherIcon;

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }
//    @SerializedName("temp")
//    public String mainTemp;
//
//    public String getMainTemp() {
//        return mainTemp;
//    }
//
//    public void setMainTemp(String mainTemp) {
//        this.mainTemp = mainTemp;
//    }
}
