package com.software.eric.coolweather.activity.weather.model;

import com.software.eric.coolweather.beans.china.WeatherInfoBean;
import com.software.eric.coolweather.model.County;

/**
 * Created by Mzz on 2016/2/10.
 */
public interface IWeatherInfoModel {
    boolean checkCountySelected();
    County loadCounty();
    void save(WeatherInfoBean weatherInfoBean);
    void queryFromServer(final String address, final int type, final WeatherInfoModelImpl.onLoadWeatherInfoListener listener);
}
