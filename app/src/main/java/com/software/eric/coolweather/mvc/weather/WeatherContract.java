package com.software.eric.coolweather.mvc.weather;

import android.support.annotation.Nullable;

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

        void saveWeatherInfo(WeatherInfo weatherInfoBean);

        /**
         * load weatherInfo from prefs
         *
         * @return weatherInfoBean
         */
        @Nullable
        WeatherInfo loadWeatherInfo();

        void queryWeatherAutoIp(String key, WeatherInfoModelImpl.onLoadWeatherInfoListener listener);

        void queryFromServer(final String address, final int type, final WeatherInfoModelImpl.onLoadWeatherInfoListener listener);
    }

    /**
     * Created by Mzz on 2016/2/7.
     */
    interface IWeatherView {

        void showWeather(HeWeather weatherInfo);

        void showFailed();

        void showSyncing();

        void setRefreshing(boolean isRefreshing);

        void initView();

        void goSetApiKey();
    }
}
