package com.software.eric.coolweather.mvc.weather;

import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.HeWeather;
import com.software.eric.coolweather.entity.WeatherInfo;

/**
 * Created by Mzz on 2016/7/8.
 */
public interface WeatherContract {

    /**
     * Created by Mzz on 2016/2/10.
     */
    interface IWeatherInfoModel {
        boolean checkCountySelected();
        County loadCounty();
        void saveWeatherInfo(WeatherInfo weatherInfoBean);

        /**
         * load weatherInfo from prefs
         * @return weatherInfoBean
         */
        WeatherInfo loadWeatherInfo();
        void queryFromServer(final String address, final int type, final WeatherInfoModelImpl.onLoadWeatherInfoListener listener);
    }

    /**
     * Created by Mzz on 2016/2/7.
     */
    interface IWeatherView {
        void showWeather(HeWeather weatherInfo);
        void showFailed();
        void showSyncing();
        void goChooseArea();
        void setRefreshing(boolean isRefreshing);
        void initView();
    }
}
