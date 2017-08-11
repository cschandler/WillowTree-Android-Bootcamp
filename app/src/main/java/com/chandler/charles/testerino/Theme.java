package com.chandler.charles.testerino;

import android.util.Log;

/**
 * Created by charleschandler on 8/9/17.
 */

class Theme {

    private int backgroundResource;
    private String icon;

    /**
     * A default theme.
     */
    Theme() {
        Log.d("theme", "Code: no selection");
        backgroundResource = R.drawable.fewclouds;
    }

    /**
     * Theme populated by data from the API.
     *
     * @param weather Model object from the API.
     */
    Theme(Weather weather) {
        if (weather == null || weather.getIcon() == null) {
            Log.d("theme", "Code: no selection");
            backgroundResource = R.drawable.fewclouds;
        }

        this.icon = weather.getIcon();

        Log.d("theme", "Icon: " + this.icon);

        String codeString = weather.getIcon().substring(0, 2);

        Log.d("theme", "Code: " + codeString);

        int code = Integer.valueOf(codeString);

        switch (code) {
            case 1:
                backgroundResource = R.drawable.fewclouds;
                break;
            case 2:
                backgroundResource = R.drawable.clearsky;
                break;
            case 3:
                backgroundResource = R.drawable.scatteredclouds;
                break;
            case 4:
                backgroundResource = R.drawable.brokenclouds;
                break;
            case 9:
                backgroundResource = R.drawable.showers;
                break;
            case 10:
                backgroundResource = R.drawable.rain;
                break;
            case 11:
                backgroundResource = R.drawable.thunderstorm;
                break;
            case 13:
                backgroundResource = R.drawable.snow;
                break;
            case 50:
                backgroundResource = R.drawable.mist;
                break;
            default:
                backgroundResource = R.drawable.noselection;
        }
    }

    int getBackgroundResource() {
        return backgroundResource;
    }

    public String getIcon() {
        return icon;
    }
}
