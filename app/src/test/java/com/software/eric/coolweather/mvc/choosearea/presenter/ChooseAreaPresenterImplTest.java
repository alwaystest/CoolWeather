package com.software.eric.coolweather.mvc.choosearea.presenter;

import com.software.eric.coolweather.mvc.choosearea.ChooseAreaContract;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaModelImpl;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaPresenterImpl;
import com.software.eric.coolweather.mvc.choosearea.ChooseAreaActivity;
import com.software.eric.coolweather.entity.City;
import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.Province;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Mzz on 2016/7/5.
 */
public class ChooseAreaPresenterImplTest {
    ChooseAreaContract.IChooseAreaModel mChooseAreaModel;
    ChooseAreaContract.IChooseAreaView mChooseAreaView;
    ChooseAreaPresenterImpl chooseAreaPresenter;

    @Before
    public void setUp() throws Exception {
        mChooseAreaModel = mock(ChooseAreaContract.IChooseAreaModel.class);
        mChooseAreaView = mock(ChooseAreaContract.IChooseAreaView.class);
        chooseAreaPresenter = new ChooseAreaPresenterImpl(mChooseAreaView, mChooseAreaModel);
    }

    @Test
    public void testSaveSelectedCounty() throws Exception {
        chooseAreaPresenter.saveSelectedCounty(new County());
        verify(mChooseAreaModel).saveSelectedCounty(any(County.class));
    }

    @Test
    public void testCheckCountySelected() throws Exception {
        chooseAreaPresenter.checkCountySelected();
        verify(mChooseAreaModel).checkCountySelected();
    }

    @Test
    public void testQueryProvinces() throws Exception {
        chooseAreaPresenter.queryProvinces();
        verify(mChooseAreaView).showProgressDialog();
        verify(mChooseAreaModel).queryProvinces(any(ChooseAreaModelImpl.OnLoadListListener.class));
        verify(mChooseAreaView).setTitle(any(String.class));
        verify(mChooseAreaModel).getString(any(Integer.class));
        verify(mChooseAreaView).setCurrentLevel(ChooseAreaActivity.LEVEL_PROVINCE);
    }

    @Test
    public void testQueryCities() throws Exception {
        Province p = new Province();
        p.setName("北京");
        chooseAreaPresenter.queryCities(p);
        verify(mChooseAreaView).showProgressDialog();
        verify(mChooseAreaModel).queryCities(eq(p), any(ChooseAreaModelImpl.OnLoadListListener.class));
        verify(mChooseAreaView).setTitle(p.getName());
        verify(mChooseAreaView).setCurrentLevel(ChooseAreaActivity.LEVEL_CITY);
    }

    @Test
    public void testQueryCounties() throws Exception {
        City c = new City();
        c.setName("朝阳");
        chooseAreaPresenter.queryCounties(c);
        verify(mChooseAreaView).showProgressDialog();
        verify(mChooseAreaModel).queryCounties(eq(c), any(ChooseAreaModelImpl.OnLoadListListener.class));
        verify(mChooseAreaView).setTitle(c.getName());
        verify(mChooseAreaView).setCurrentLevel(ChooseAreaActivity.LEVEL_COUNTY);
    }

    @Test
    public void shouldGoToWeatherActivity1() throws Exception {
        when(mChooseAreaModel.checkCountySelected()).thenReturn(true);
        chooseAreaPresenter.checkIfGoToWeatherActivity(false);
        verify(mChooseAreaView).goToWeatherActivity();
        verify(mChooseAreaView, times(0)).initView();
        verify(mChooseAreaView, times(0)).showProgressDialog();
    }

    @Test
    public void shouldNotGoToWeatherActivity1() throws Exception {
        when(mChooseAreaModel.checkCountySelected()).thenReturn(false);
        chooseAreaPresenter.checkIfGoToWeatherActivity(false);
        verify(mChooseAreaView, times(0)).goToWeatherActivity();
        verify(mChooseAreaView).initView();
        verify(mChooseAreaView).showProgressDialog();
    }

    @Test
    public void shouldNotGoToWeatherActivity2() throws Exception {
        when(mChooseAreaModel.checkCountySelected()).thenReturn(true);
        chooseAreaPresenter.checkIfGoToWeatherActivity(true);
        verify(mChooseAreaView, times(0)).goToWeatherActivity();
        verify(mChooseAreaView).initView();
        verify(mChooseAreaView).showProgressDialog();
    }

    @Test
    public void shouldNotGoToWeatherActivity3() throws Exception {
        when(mChooseAreaModel.checkCountySelected()).thenReturn(false);
        chooseAreaPresenter.checkIfGoToWeatherActivity(true);
        verify(mChooseAreaView, times(0)).goToWeatherActivity();
        verify(mChooseAreaView).initView();
        verify(mChooseAreaView).showProgressDialog();
    }

    @Test
    public void testOnListItemClicked1() throws Exception {
        Province p = new Province();
        when(mChooseAreaView.getCurrentLevel()).thenReturn(ChooseAreaActivity.LEVEL_PROVINCE);
        chooseAreaPresenter.onListItemClicked(p);
        verify(mChooseAreaView).setSelectedAddress(p);
        verify(mChooseAreaView).setCurrentLevel(ChooseAreaActivity.LEVEL_CITY);
    }

    @Test
    public void testOnListItemClicked2() throws Exception {
        City c = new City();
        when(mChooseAreaView.getCurrentLevel()).thenReturn(ChooseAreaActivity.LEVEL_CITY);
        chooseAreaPresenter.onListItemClicked(c);
        verify(mChooseAreaView).setSelectedAddress(c);
        verify(mChooseAreaView).setCurrentLevel(ChooseAreaActivity.LEVEL_COUNTY);
    }

    @Test
    public void testOnListItemClicked3() throws Exception {
        County c = new County();
        c.setName("name");
        c.setCode("code");
        ArgumentCaptor<County> captor = ArgumentCaptor.forClass(County.class);
        when(mChooseAreaView.getCurrentLevel()).thenReturn(ChooseAreaActivity.LEVEL_COUNTY);
        chooseAreaPresenter.onListItemClicked(c);
        verify(mChooseAreaModel).saveSelectedCounty(captor.capture());
        assertEquals("name", captor.getValue().getName());
        assertEquals("code", captor.getValue().getCode());
        verify(mChooseAreaView).goToWeatherActivity();
    }

    @Test
    public void testOnSuccess() throws Exception {
        List<Province> l = new ArrayList<>();
        chooseAreaPresenter.onSuccess(l);
        verify(mChooseAreaView).setList(l);
        verify(mChooseAreaView).closeProgressDialog();
    }
}