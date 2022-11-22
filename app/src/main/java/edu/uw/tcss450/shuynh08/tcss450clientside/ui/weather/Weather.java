package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import java.io.Serializable;

public class Weather implements Serializable {

    private final String mWeatherType;
    private final String mWeatherDescription;
    private final double mTemperature;
    private final String mCity;
    private final String mTime;
    private final int mIcon;

    public Weather(String weatherType, String weatherDescription, double temperature, String city, String time, int icon) {
        this.mWeatherType = weatherType;
        this.mWeatherDescription = weatherDescription;
        this.mTemperature = temperature;
        this.mCity = city;
        this.mTime = time;
        this.mIcon = icon;
    }

    public Weather (final Builder builder) {
        this.mWeatherType = builder.mWeatherType;
        this.mWeatherDescription = builder.mWeatherDescription;
        this.mTemperature = builder.mTemperature;
        this.mCity = builder.mCity;
        this.mTime = builder.mTime;
        this.mIcon = builder.mIcon;
    }

    public static class Builder {
        private final String mWeatherType;
        private final String mWeatherDescription;
        private double mTemperature = 0.0;
        private String mCity = "";
        private String mTime = "";
        private int mIcon;

        public Builder(String weatherType, String weatherDescription) {
            this.mWeatherType = weatherType;
            this.mWeatherDescription = weatherDescription;
        }

        public Builder addTemperature(final double temperature) {
            mTemperature = temperature;
            return this;
        }

        public Builder addCity(final String city) {
            mCity = city;
            return this;
        }



        public Weather build() {
            return new Weather(this);
        }

    }

    public String getWeatherType() {
        return mWeatherType;
    }

    public String getWeatherDescription() {
        return mWeatherDescription;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public String getCity() {
        return mCity;
    }

    public String getTime() {
        return mTime;
    }

    public int getIcon() {
        return mIcon;
    }
}
