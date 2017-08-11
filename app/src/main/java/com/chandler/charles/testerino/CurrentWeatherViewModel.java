package com.chandler.charles.testerino;

import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;

/**
 * Created by charleschandler on 8/9/17.
 */

public class CurrentWeatherViewModel {

    private CurrentWeather currentWeather;
    private Theme theme;
    private boolean isDefault;

    /**
     * A default view model.
     */
    public CurrentWeatherViewModel() {
        defaultInit();
    }

    /**
     * View model that is populated with data from the API.
     *
     * @param currentWeather API model class.
     */
    CurrentWeatherViewModel(CurrentWeather currentWeather) {
        if (currentWeather == null || currentWeather.getWeather() == null) {
            defaultInit();
            return;
        }

        this.currentWeather = currentWeather;

        if (currentWeather.getWeather().size() == 1) {
            Weather weather = currentWeather.getWeather().get(0);
            this.theme = new Theme(weather);
        } else {
            defaultInit();
            Log.d("1234", "Current weather did not return the correct number of weather items.");
        }

    }

    private void defaultInit() {
        this.theme = new Theme();
        this.isDefault = true;
    }

    String getCityName() {
        if (currentWeather == null || currentWeather.getName() == null) {
            return "";
        }

        return currentWeather.getName();
    }

    String getCurrentTemp() {
        if (currentWeather == null || currentWeather.getMain() == null) {
            return "";
        }

        int temp = (int) currentWeather.getMain().getTemp();

        String DEGREES_SYMBOL = "°";
        return String.valueOf(temp) + DEGREES_SYMBOL;
    }

    String getCondition() {
        if (currentWeather == null ||
            currentWeather.getMain() == null ||
            currentWeather.getWeather() == null ||
            currentWeather.getWeather().size() == 0) {
                return "";
        }

        Weather weather = currentWeather.getWeather().get(0);
        String condition = weather.getDescription();
        int max = (int) currentWeather.getMain().getTempMax();
        int min = (int) currentWeather.getMain().getTempMin();
        CharSequence detail = makeDetailText(condition, min, max);

        return detail.toString();
    }

    DraweeController getBackgroundController() {
        return setupBackground(theme.getBackgroundResource());
    }

    boolean isDefault() {
        return isDefault;
    }

    String getIcon() {
        return theme.getIcon();
    }

    /**
     * Creates the detail string with sized text.
     *
     * @param minTemp The degrees to display before the slash. Min temp.
     * @param maxTemp The degrees to display after the slash. Max temp.
     * @return Detail text string.
     */
    private CharSequence makeDetailText(String condition, int minTemp, int maxTemp) {
        SpannableString span1 = new SpannableString(condition + "  " + Integer.toString(minTemp) + "°");
        SpannableString span2 = new SpannableString("/");
        SpannableString span3 = new SpannableString(Integer.toString(maxTemp) + "°");

        span3.setSpan(new RelativeSizeSpan(0.8f), 0, 3, 0);

        return TextUtils.concat(span1, " ", span2, " ", span3);
    }

    /**
     * Creates the background animation for the weather condition.
     *
     * @param resource id for the webp resource.
     */
    private DraweeController setupBackground(int resource) {
        final Uri uri = UriUtil.getUriForResourceId(resource);

        return Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
    }

}
