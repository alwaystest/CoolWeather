package com.software.eric.coolweather.di;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by eric on 2018/11/23
 */
@Component(modules = {SingletonModule.class})
@Singleton
public interface SingletonComponent {
    Gson getGson();
}
