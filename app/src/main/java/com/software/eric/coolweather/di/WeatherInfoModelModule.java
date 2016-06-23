package com.software.eric.coolweather.di;

import com.software.eric.coolweather.activity.weather.model.IWeatherInfoModel;
import com.software.eric.coolweather.activity.weather.model.WeatherInfoModelImpl;
import com.software.eric.coolweather.activity.weather.presenter.IWeatherPresenter;
import com.software.eric.coolweather.activity.weather.presenter.WeatherPresenterImpl;
import com.software.eric.coolweather.activity.weather.view.IWeatherView;

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
