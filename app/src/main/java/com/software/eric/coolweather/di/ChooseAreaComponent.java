package com.software.eric.coolweather.di;

import com.software.eric.coolweather.mvc.choosearea.ChooseAreaActivity;

import dagger.Component;

/**
 * Created by Mzz on 2016/7/15.
 */
@Component(modules = {ChooseAreaModule.class})
public interface ChooseAreaComponent {
    void inject(ChooseAreaActivity activity);
}
