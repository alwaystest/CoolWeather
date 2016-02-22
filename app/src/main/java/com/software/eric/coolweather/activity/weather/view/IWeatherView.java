package com.software.eric.coolweather.activity.weather.view;

import com.software.eric.coolweather.beans.china.WeatherInfoBean;

/**
 * Created by Mzz on 2016/2/7.
 */
public interface IWeatherView {
    void showWeather(WeatherInfoBean weatherInfo);
    void showFailed();
    void showSyncing();
    void goChooseArea();
    void setRefreshing(boolean isRefreshing);
}
