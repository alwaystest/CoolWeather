package com.software.eric.coolweather.di;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by eric on 2018/11/20
 */
@Module
public class SingletonModule {
    @Provides
    @Singleton
    static Gson provideGson() {
        return new Gson();
    }
}
