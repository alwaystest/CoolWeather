package com.software.eric.coolweather.activity.choosearea.presenter;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.activity.choosearea.model.ChooseAreaModelImpl;
import com.software.eric.coolweather.activity.choosearea.model.IChooseAreaModel;
import com.software.eric.coolweather.activity.choosearea.view.ChooseAreaActivity;
import com.software.eric.coolweather.activity.choosearea.view.IChooseAreaView;
import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.util.MyApplication;

import java.util.List;

/**
 * Created by Mzz on 2016/2/15.
 */
public class ChooseAreaPresenterImpl implements IChooseAreaPresenter, ChooseAreaModelImpl.OnLoadListListener {
    IChooseAreaModel mChooseAreaModel;
    IChooseAreaView mChooseAreaView;

    public ChooseAreaPresenterImpl(IChooseAreaView ChooseAreaView) {
        mChooseAreaModel = new ChooseAreaModelImpl();
        mChooseAreaView = ChooseAreaView;
    }

    @Override
    public void saveSelectedCounty(County county) {
        mChooseAreaModel.saveSelectedCounty(county);
    }

    @Override
    public boolean checkCountySelected() {
        return mChooseAreaModel.checkCountySelected();
    }

    @Override
    public void queryProvinces() {
        mChooseAreaView.showProgressDialog();
        mChooseAreaModel.queryProvinces(this);
        mChooseAreaView.setTitle(MyApplication.getContext().getString(R.string.china));
        mChooseAreaView.setCurrentLevel(ChooseAreaActivity.LEVEL_PROVINCE);
    }

    @Override
    public void queryCities(Address selectedProvince) {
        mChooseAreaView.showProgressDialog();
        mChooseAreaModel.queryCities(selectedProvince, this);
        mChooseAreaView.setTitle(selectedProvince.getName());
        mChooseAreaView.setCurrentLevel(ChooseAreaActivity.LEVEL_CITY);
    }

    @Override
    public void queryCounties(Address selectedCity) {
        mChooseAreaView.showProgressDialog();
        mChooseAreaModel.queryCounties(selectedCity, this);
        mChooseAreaView.setTitle(selectedCity.getName());
        mChooseAreaView.setCurrentLevel(ChooseAreaActivity.LEVEL_COUNTY);
    }

    @Override
    public void checkIfGoToWeatherActivity(boolean isFromWeatherActivity) {
        if(!isFromWeatherActivity &&checkCountySelected()){
            mChooseAreaView.goToWeatherActivity();
        }else{
            mChooseAreaView.initView();
            queryProvinces();
        }
    }

    @Override
    public void onListItemClicked(Address selectedAddress) {
        int currentLevel = mChooseAreaView.getCurrentLevel();
        if (currentLevel == ChooseAreaActivity.LEVEL_PROVINCE) {
            mChooseAreaView.setSelectedAddress(selectedAddress);
            queryCities(selectedAddress);
        } else if (currentLevel == ChooseAreaActivity.LEVEL_CITY) {
            mChooseAreaView.setSelectedAddress(selectedAddress);
            queryCounties(selectedAddress);
        } else if (currentLevel == ChooseAreaActivity.LEVEL_COUNTY) {
            String countyName = selectedAddress.getName();
            String countyCode = selectedAddress.getCode();
            County county = new County();
            county.setCode(countyCode);
            county.setName(countyName);
            saveSelectedCounty(county);
            mChooseAreaView.goToWeatherActivity();
        }
    }

    @Override
    public void onSuccess(List<? extends Address> list) {
        mChooseAreaView.setList(list);
        mChooseAreaView.closeProgressDialog();
    }

    @Override
    public void onFailure() {

    }
}
