package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.weather.IWeatherInfoModel;
import com.software.eric.coolweather.mvc.weather.IWeatherPresenter;
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
    IWeatherInfoModel provideWeatherInfoModel(){
        return new WeatherInfoModelImpl();
    }

    @Provides
    IWeatherPresenter provideWeatherPresenter(IWeatherInfoModel weatherInfoModel) {
        return new WeatherPresenterImpl(weatherInfoModel);
    }
}
