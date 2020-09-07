package com.example.myweatherapplication.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherDay {

    public class WeatherTemp {
        Double temp;
    }

    public class WeatherDescription {
        String icon;
        String description;
    }

    @SerializedName("main")
    private WeatherTemp temp;

    @SerializedName("weather")
    private List<WeatherDescription> weatherDescriptions;
    private WeatherDescription description;


    public void setWeatherDescriptions(List<WeatherDescription> weatherDescriptions) {
        this.weatherDescriptions = weatherDescriptions;
    }

    @SerializedName("name")
    private String city;

    @SerializedName("dt")
    private long timestamp;

    public WeatherDay(WeatherTemp temp, WeatherDescription description) {
        this.temp = temp;
    }


    public String getTemp() {
        return String.valueOf(temp.temp);
    }

    public String getDescription() {
        return weatherDescriptions.get(0).description;
    }


    public String getTempWithDegree() {
        return String.valueOf(temp.temp.intValue()) + "\u00B0";
    }

    public String getCity() {
        return city;
    }

    public String getIcon() {
        return weatherDescriptions.get(0).icon;
    }

    public String getIconUrl() {
        return "https://openweathermap.org/img/w/" + weatherDescriptions.get(0).icon + ".png";
    }

    public List<WeatherDescription> getWeatherDescriptions() {
        return weatherDescriptions;
    }
}
