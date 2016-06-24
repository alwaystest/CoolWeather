package com.software.eric.coolweather.activity.weather.model;

import com.software.eric.coolweather.beans.china.WeatherInfoBean;
import com.software.eric.coolweather.model.County;

/**
 * Created by Mzz on 2016/2/10.
 */
public interface IWeatherInfoModel {
    boolean checkCountySelected();
    County loadCounty();
    void saveWeatherInfo(WeatherInfoBean weatherInfoBean);

    /**
     * load weatherInfo from prefs
     * @return weatherInfoBean
     */
    WeatherInfoBean loadWeatherInfo();
    void queryFromServer(final String address, final int type, final WeatherInfoModelImpl.onLoadWeatherInfoListener listener);
}
