package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.weather.WeatherActivity;

import dagger.Component;

/**
 * Created by Mzz on 2016/6/23.
 */
@Component(modules = {WeatherInfoModule.class})
public interface WeatherModelComponent {
    void inject(WeatherActivity weatherActivity);
}
