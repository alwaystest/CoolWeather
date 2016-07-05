package com.software.eric.coolweather.activity.choosearea.presenter;

import com.software.eric.coolweather.activity.choosearea.model.ChooseAreaModelImpl;
import com.software.eric.coolweather.activity.choosearea.model.IChooseAreaModel;
import com.software.eric.coolweather.activity.choosearea.view.ChooseAreaActivity;
import com.software.eric.coolweather.activity.choosearea.view.IChooseAreaView;
import com.software.eric.coolweather.model.County;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Mzz on 2016/7/5.
 */
public class ChooseAreaPresenterImplTest {
    IChooseAreaModel mChooseAreaModel;
    IChooseAreaView mChooseAreaView;
    ChooseAreaPresenterImpl chooseAreaPresenter;

    @Before
    public void setUp() throws Exception {
        mChooseAreaModel = mock(IChooseAreaModel.class);
        mChooseAreaView = mock(IChooseAreaView.class);
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

    }

    @Test
    public void testQueryCounties() throws Exception {

    }

    @Test
    public void testCheckIfGoToWeatherActivity() throws Exception {

    }

    @Test
    public void testOnListItemClicked() throws Exception {

    }

    @Test
    public void testOnSuccess() throws Exception {

    }

    @Test
    public void testOnFailure() throws Exception {

    }
}