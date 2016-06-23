package com.software.eric.coolweather.activity.weather.presenter;

/**
 * Created by Mzz on 2016/2/7.
 */
public interface IWeatherPresenter {
    void queryWeather(boolean isRefresh);
    void setAutoUpdateService();
    boolean checkCountySelected();
    void ifGoChooseArea();
}
