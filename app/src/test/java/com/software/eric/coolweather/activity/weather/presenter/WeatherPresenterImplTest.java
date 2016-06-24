package com.software.eric.coolweather.activity.weather.presenter;

import com.software.eric.coolweather.activity.weather.model.IWeatherInfoModel;
import com.software.eric.coolweather.activity.weather.view.IWeatherView;
import com.software.eric.coolweather.beans.china.WeatherInfoBean;
import com.software.eric.coolweather.model.County;

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
        County c = new County();
        c.setName("Teat");
        when(weatherInfoModel.loadCounty()).thenReturn(c);
    }

    @Test
    public void shouldInitView() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.ifGoChooseArea();
        Mockito.verify(weatherView).initView();
        Mockito.verify(weatherView, times(0)).goChooseArea();
    }

    @Test
    public void shouldGoChooseArea() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(false);
        weatherPresenter.ifGoChooseArea();
        Mockito.verify(weatherView, times(0)).initView();
        Mockito.verify(weatherView).goChooseArea();
    }

    @Test
    public void shouldCheckCountySelected() throws Exception {
        weatherPresenter.ifGoChooseArea();
        Mockito.verify(weatherInfoModel).checkCountySelected();
    }

    @Test
    public void shouldCheckCountySelected1() throws Exception {
        weatherPresenter.queryWeather(true);
        Mockito.verify(weatherInfoModel).checkCountySelected();
    }

    @Test
    public void shouldLoadWeatherInfo() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.queryWeather(false);
        Mockito.verify(weatherInfoModel).loadWeatherInfo();
    }

    @Test
    public void shouldNotShowWeather() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        when(weatherInfoModel.loadWeatherInfo()).thenReturn(null);
        weatherPresenter.queryWeather(true);
        Mockito.verify(weatherView, times(0)).setRefreshing(false);
    }

    @Test
    public void shouldShowWeather() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        WeatherInfoBean w = new WeatherInfoBean();
        when(weatherInfoModel.loadWeatherInfo()).thenReturn(w);
        weatherPresenter.queryWeather(false);
        Mockito.verify(weatherView).setRefreshing(false);
        Mockito.verify(weatherView).showWeather(w);
    }
}