package com.software.eric.coolweather.di;

import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by eric on 2018/11/20
 */
@Module
public class SingletonModule {
    @Provides
    @Singleton
    static Gson proviceGson() {
        return new Gson();
    }
}
