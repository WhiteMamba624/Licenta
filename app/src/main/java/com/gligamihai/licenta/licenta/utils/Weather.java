package com.gligamihai.licenta.licenta.utils;

import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("temp")
    String temp;

    @SerializedName("temp_min")
    String tempMin;

    @SerializedName("temp_max")
    String tempMax;

    @SerializedName("pressure")
    String tempPressure;

    @SerializedName("humidity")
    String tempHumidity;

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempPressure() {
        return tempPressure;
    }

    public void setTempPressure(String tempPressure) {
        this.tempPressure = tempPressure;
    }

    public String getTempHumidity() {
        return tempHumidity;
    }

    public void setTempHumidity(String tempHumidity) {
        this.tempHumidity = tempHumidity;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }
//    @SerializedName("id")
//    public int weatherId;
//
//    @SerializedName("main")
//    public String weatherMain;
//
//    public int getWeatherId() {
//        return weatherId;
//    }
//
//    public void setWeatherId(int weatherId) {
//        this.weatherId = weatherId;
//    }
//
//    public String getWeatherMain() {
//        return weatherMain;
//    }
//
//    public void setWeatherMain(String weatherMain) {
//        this.weatherMain = weatherMain;
//    }
}
