package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.choosearea.ChooseAreaContract;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaModelImpl;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mzz on 2016/7/15.
 */
@Module
public class ChooseAreaModule {
    @Provides
    ChooseAreaContract.IChooseAreaModel provideChooseAreaModule() {
        return new ChooseAreaModelImpl();
    }

    @Provides
    ChooseAreaContract.IChooseAreaPresenter provideChooseAreaPresenter(ChooseAreaContract.IChooseAreaModel model) {
        return new ChooseAreaPresenterImpl(model);
    }
}
