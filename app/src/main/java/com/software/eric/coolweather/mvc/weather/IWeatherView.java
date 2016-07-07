package com.software.eric.coolweather.mvc.weather;

import com.software.eric.coolweather.entity.WeatherInfoBean;

/**
 * Created by Mzz on 2016/2/7.
 */
public interface IWeatherView {
    void showWeather(WeatherInfoBean weatherInfo);
    void showFailed();
    void showSyncing();
    void goChooseArea();
    void setRefreshing(boolean isRefreshing);
    void initView();
}
