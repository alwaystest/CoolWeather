package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.weather.WeatherActivity;
import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Mzz on 2016/6/23.
 */
@Component(modules = {WeatherInfoModule.class, SingletonModule.class})
@Singleton
public interface WeatherModelComponent {
    void inject(WeatherActivity weatherActivity);
}
