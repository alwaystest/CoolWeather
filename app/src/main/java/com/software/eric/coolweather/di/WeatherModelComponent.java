package com.software.eric.coolweather.di;

import com.software.eric.coolweather.activity.weather.presenter.IWeatherPresenter;
import com.software.eric.coolweather.activity.weather.presenter.WeatherPresenterImpl;
import com.software.eric.coolweather.activity.weather.view.WeatherActivity;

import dagger.Component;

/**
 * Created by Mzz on 2016/6/23.
 */
@Component(modules = {WeatherInfoModelModule.class})
public interface WeatherModelComponent {
    void inject(WeatherActivity weatherActivity);
}
