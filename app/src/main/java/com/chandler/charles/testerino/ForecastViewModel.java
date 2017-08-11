package com.chandler.charles.testerino;

/**
 * Created by charleschandler on 8/8/17.
 */

public class ForecastViewModel {

    private Weather weather;
    private Main main;

    public ForecastViewModel(Weather weather, Main main) {
        this.weather = weather;
        this.main = main;
    }

    public String getDate() {
        return "";

    }

    public String getCondition() {
        if (weather == null) {
            return "";
        }

        return weather.getDescription();
    }

    public String getHighTemp() {
        if (weather == null || main == null) {
            return "";
        }

        return String.valueOf(main.getTempMax());
    }

    public String getLowTemp() {
        if (weather == null || main == null) {
            return "";
        }

        return String.valueOf(main.getTempMin());
    }

    public String getIconURLString() {
        return "http://openweathermap.org/img/w/" + weather.getIcon() + ".png";
    }

}
