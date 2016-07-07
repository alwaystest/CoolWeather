package com.software.eric.coolweather.mvc.weather;

import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.WeatherInfoBean;

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
