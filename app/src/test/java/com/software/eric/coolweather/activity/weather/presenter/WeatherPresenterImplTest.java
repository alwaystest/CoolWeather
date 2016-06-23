package com.software.eric.coolweather.activity.weather.presenter;

import com.software.eric.coolweather.activity.weather.model.IWeatherInfoModel;
import com.software.eric.coolweather.activity.weather.view.IWeatherView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Mzz on 2016/6/23.
 */
public class WeatherPresenterImplTest {
    WeatherPresenterImpl weatherPresenter;
    IWeatherView weatherView;
    IWeatherInfoModel weatherInfoModel;

    @Before
    public void setUp() throws Exception {
        weatherView = Mockito.mock(IWeatherView.class);
        weatherInfoModel = Mockito.mock(IWeatherInfoModel.class);
        weatherPresenter = new WeatherPresenterImpl(weatherInfoModel);
        weatherPresenter.acceptView(weatherView);
    }

    @Test
    public void shouldInitView() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.ifGoChooseArea();
        Mockito.verify(weatherView).initView();
        Mockito.verify(weatherView,times(0)).goChooseArea();
    }

    @Test
    public void shouldGoChooseArea() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(false);
        weatherPresenter.ifGoChooseArea();
        Mockito.verify(weatherView,times(0)).initView();
        Mockito.verify(weatherView).goChooseArea();
    }
}