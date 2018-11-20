package com.software.eric.coolweather.di;

import com.google.gson.Gson;
import com.software.eric.coolweather.mvc.weather.WeatherContract;
import com.software.eric.coolweather.mvc.weather.WeatherInfoModelImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Mzz on 2016/6/23.
 */
@Module
public class WeatherInfoModule {
    WeatherContract.IWeatherView mView;

    public WeatherInfoModule(WeatherContract.IWeatherView view) {
        mView = view;
    }

    @Provides
    WeatherContract.IWeatherInfoModel provideWeatherInfoModel(Gson gson){
        return new WeatherInfoModelImpl(gson);
    }

    @Provides
    WeatherContract.IWeatherView provideWeatherView() {
        return mView;
    }

}
