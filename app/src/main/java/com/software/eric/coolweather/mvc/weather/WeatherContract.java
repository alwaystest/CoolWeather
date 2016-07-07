package com.software.eric.coolweather.mvc.weather;

import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.WeatherInfoBean;

/**
 * Created by Mzz on 2016/7/8.
 */
public interface WeatherContract {
    /**
     * Created by Mzz on 2016/2/7.
     */
    interface IWeatherPresenter {
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

    /**
     * Created by Mzz on 2016/2/10.
     */
    interface IWeatherInfoModel {
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

    /**
     * Created by Mzz on 2016/2/7.
     */
    interface IWeatherView {
        void showWeather(WeatherInfoBean weatherInfo);
        void showFailed();
        void showSyncing();
        void goChooseArea();
        void setRefreshing(boolean isRefreshing);
        void initView();
    }
}
