package edu.uw.tcss450.shuynh08.tcss450clientside.ui.weather;

import java.io.Serializable;

/**
 * A model class used to represent Weather.
 */
public class Weather implements Serializable {

    /**
     * String of the weather type.
     */
    private final String mWeatherType;

    /**
     * String of the weather description.
     */
    private final String mWeatherDescription;

    /**
     * String of the current temperature.
     */
    private final double mTemperature;

    /**
     * String of the city's name.
     */
    private final String mCity;

    /**
     * String of the current time.
     */
    private final String mTime;

    /**
     * String of the URL for an icon for the current weather.
     */
    private final String mIconURL;

    /**
     * Constructor for the Weather object.
     * @param weatherType String of the weather type
     * @param weatherDescription String of the weather description
     * @param temperature String of the current temperature
     * @param city String of the city's name
     * @param time String of the current time
     * @param iconUrl String of the URL for an icon for the current weather
     */
    public Weather(String weatherType, String weatherDescription, double temperature, String city, String time, String iconUrl) {
        this.mWeatherType = weatherType;
        this.mWeatherDescription = weatherDescription;
        this.mTemperature = temperature;
        this.mCity = city;
        this.mTime = time;
        this.mIconURL = iconUrl;
    }

    /**
     * Constructor for the Weather object using a dedicated builder.
     * @param builder The dedicated builder which creates a Weather object
     */
    public Weather (final Builder builder) {
        this.mWeatherType = builder.mWeatherType;
        this.mWeatherDescription = builder.mWeatherDescription;
        this.mTemperature = builder.mTemperature;
        this.mCity = builder.mCity;
        this.mTime = builder.mTime;
        this.mIconURL = builder.mIconURL;
    }

    /**
     * Inner class for a dedicated builder to create a Weather object.
     * [This inner class may be deleted if not needed.]
     */
    public static class Builder {

        /**
         * String of the weather type.
         */
        private final String mWeatherType;

        /**
         * String of the weather description.
         */
        private final String mWeatherDescription;

        /**
         * Double of the current temperature.
         */
        private double mTemperature = 0.0;

        /**
         * String of the city's name.
         */
        private String mCity = "";

        /**
         * String of the current time.
         */
        private String mTime = "";

        /**
         * String of the URL for an icon for the current weather.
         */
        private String mIconURL;

        /**
         * Builder constructor for the Weather object.
         * @param weatherType String of the weather type
         * @param weatherDescription String of the weather description
         */
        public Builder(String weatherType, String weatherDescription) {
            this.mWeatherType = weatherType;
            this.mWeatherDescription = weatherDescription;
        }

        /**
         * Adds the temperature.
         * @param temperature Double of the current temperature
         * @return The current build with the temperature now applied
         */
        public Builder addTemperature(final double temperature) {
            mTemperature = temperature;
            return this;
        }

        /**
         * Adds the city.
         * @param city String of the city's name.
         * @return he current build with the city now applied
         */
        public Builder addCity(final String city) {
            mCity = city;
            return this;
        }

        /**
         * @return A Weather object based on the current build.
         */
        public Weather build() {
            return new Weather(this);
        }

    }

    /**
     * @return String of the weather type.
     */
    public String getWeatherType() {
        return mWeatherType;
    }

    /**
     * @return String of the weather description.
     */
    public String getWeatherDescription() {
        return mWeatherDescription;
    }

    /**
     * @return Double of the current temperature.
     */
    public double getTemperature() {
        return mTemperature;
    }

    /**
     * @return String of the city's name.
     */
    public String getCity() {
        return mCity;
    }

    /**
     * @return String of the current time.
     */
    public String getTime() {
        return mTime;
    }

    /**
     * @return String of the URL for an icon for the current weather.
     */
    public String getIconUrl() {
        return mIconURL;
    }
}
