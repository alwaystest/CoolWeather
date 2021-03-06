package com.software.eric.coolweather.mvc.weather;

import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.WeatherInfoBean;
import com.software.eric.coolweather.mvc.key.KeyContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by Mzz on 2016/6/23.
 */
public class WeatherPresenterImplTest {
    WeatherPresenterImpl weatherPresenter;
    WeatherContract.IWeatherView weatherView;
    WeatherContract.IWeatherInfoModel weatherInfoModel;
    KeyContract.Repo keyRepo;

    @Before
    public void setUp() throws Exception {
        weatherView = mock(WeatherContract.IWeatherView.class);
        weatherInfoModel = mock(WeatherContract.IWeatherInfoModel.class);
        keyRepo = mock(KeyContract.Repo.class);
        weatherPresenter = new WeatherPresenterImpl(weatherView, weatherInfoModel, keyRepo);
    }

    @Test
    public void shouldInitView() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.init(); // <== This
        Mockito.verify(weatherView).initView();
        Mockito.verify(weatherView, times(0)).goChooseArea();
    }

    @Test
    public void shouldGoChooseArea() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(false);
        weatherPresenter.init(); // <== This
        Mockito.verify(weatherView, times(0)).initView();
        Mockito.verify(weatherView).goChooseArea();
    }

    @Test
    public void shouldCheckCountySelected() throws Exception {
        weatherPresenter.init(); // <== This
        Mockito.verify(weatherInfoModel).checkCountySelected();
    }

    @Test
    public void shouldCheckCountySelected1() throws Exception {
        weatherPresenter.queryWeather(true); // <== This
        Mockito.verify(weatherInfoModel).checkCountySelected();
    }

    @Test
    public void shouldLoadWeatherInfo() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.queryWeather(false); // <== This
        Mockito.verify(weatherInfoModel).loadWeatherInfo();
        Mockito.verify(weatherInfoModel).loadCounty();
        Mockito.verify(weatherView).showSyncing();
    }

    @Test
    public void shouldNotShowWeather() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        when(weatherInfoModel.loadWeatherInfo()).thenReturn(null);
        weatherPresenter.queryWeather(false); // <== This
        Mockito.verify(weatherView, times(0)).setRefreshing(false);
        Mockito.verify(weatherView, times(0)).showWeather(any(WeatherInfoBean.class));
    }

    @Test
    public void shouldNotShowWeather1() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        when(weatherInfoModel.loadWeatherInfo()).thenReturn(new WeatherInfoBean());
        weatherPresenter.queryWeather(true); // <== This
        Mockito.verify(weatherView, times(0)).setRefreshing(false);
        Mockito.verify(weatherView, times(0)).showWeather(any(WeatherInfoBean.class));
    }

    @Test
    public void shouldShowWeather() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        when(weatherInfoModel.loadWeatherInfo()).thenReturn(new WeatherInfoBean());
        weatherPresenter.queryWeather(false);// <== This
        Mockito.verify(weatherView).setRefreshing(false);
        Mockito.verify(weatherView).showWeather(any(WeatherInfoBean.class));
    }

    @Test
    public void shouldGoChooseArea1() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(false);
        weatherPresenter.queryWeather(false); // <== This
        Mockito.verify(weatherView, times(0)).setRefreshing(anyBoolean());
        Mockito.verify(weatherView, times(0)).showWeather(any(WeatherInfoBean.class));
        Mockito.verify(weatherView).showFailed();
        Mockito.verify(weatherView).goChooseArea();
        Mockito.verify(weatherInfoModel, times(0)).loadWeatherInfo();
        Mockito.verify(weatherInfoModel, times(0)).loadCounty();
        Mockito.verify(weatherView, times(0)).showSyncing();
    }

    @Test
    public void testOnFinish() throws Exception {
        weatherPresenter.onFinish(new WeatherInfoBean()); // <== This
        Mockito.verify(weatherView).setRefreshing(false);
        Mockito.verify(weatherView).showWeather(any(WeatherInfoBean.class));
    }

    @Test
    public void testOnError() throws Exception {
        weatherPresenter.onError();
        Mockito.verify(weatherView).setRefreshing(false);
        Mockito.verify(weatherView).showFailed();
    }

    @Test
    public void shouldNotShowSync() throws Exception {
        County c = new County();
        when(weatherInfoModel.loadCounty()).thenReturn(c);
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.queryWeather(false); // <== This
        Mockito.verify(weatherView, times(0)).showSyncing();
    }

    @Test
    public void shouldQueryFromServer() throws Exception {
        when(weatherInfoModel.checkCountySelected()).thenReturn(true);
        weatherPresenter.queryWeather(false); // <== This
        Mockito.verify(weatherInfoModel).queryFromServer(anyString(),anyInt(),any(WeatherInfoModelImpl.onLoadWeatherInfoListener.class));
    }
}