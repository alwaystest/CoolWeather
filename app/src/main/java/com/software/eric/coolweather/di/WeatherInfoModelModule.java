package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.weather.WeatherContract;
import com.software.eric.coolweather.mvc.weather.WeatherInfoModelImpl;
import com.software.eric.coolweather.mvc.weather.WeatherPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mzz on 2016/6/23.
 */
@Module
public class WeatherInfoModelModule {

    @Provides
    WeatherContract.IWeatherInfoModel provideWeatherInfoModel(){
        return new WeatherInfoModelImpl();
    }

    @Provides
    WeatherContract.IWeatherPresenter provideWeatherPresenter(WeatherContract.IWeatherInfoModel weatherInfoModel) {
        return new WeatherPresenterImpl(weatherInfoModel);
    }
}
