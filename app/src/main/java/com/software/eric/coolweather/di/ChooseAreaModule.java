package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.choosearea.ChooseAreaContract;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaModelImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mzz on 2016/7/15.
 */
@Module
public class ChooseAreaModule {
    ChooseAreaContract.IChooseAreaView mChooseAreaView;

    public ChooseAreaModule(ChooseAreaContract.IChooseAreaView view) {
        mChooseAreaView = view;
    }

    @Provides
    ChooseAreaContract.IChooseAreaModel provideChooseAreaModule() {
        return new ChooseAreaModelImpl();
    }

    @Provides
    ChooseAreaContract.IChooseAreaView provideChooseAreaView() {
        return mChooseAreaView;
    }

}
