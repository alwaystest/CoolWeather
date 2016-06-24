package com.software.eric.coolweather.activity.weather.presenter;

import com.software.eric.coolweather.activity.weather.view.IWeatherView;

/**
 * Created by Mzz on 2016/2/7.
 */
public interface IWeatherPresenter {
    void acceptView(IWeatherView weatherView);

    /**
     * query weather.
     * load from prefs or internet
     * @param isRefresh if is swipe refresh.
     */
    void queryWeather(boolean isRefresh);
    void setAutoUpdateService();
    boolean checkCountySelected();
    void ifGoChooseArea();
}
