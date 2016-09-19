package com.software.eric.coolweather.mvc.choosearea;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.entity.Address;
import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.util.LogUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mzz on 2016/2/15.
 */
public class ChooseAreaPresenterImpl implements ChooseAreaContract.IChooseAreaPresenter, ChooseAreaModelImpl.OnLoadListListener {
    private static final String TAG = "ChooseAreaPresenterImpl";
    ChooseAreaContract.IChooseAreaModel mChooseAreaModel;
    ChooseAreaContract.IChooseAreaView mChooseAreaView;

    @Inject
    public ChooseAreaPresenterImpl(ChooseAreaContract.IChooseAreaView view, ChooseAreaContract.IChooseAreaModel chooseAreaModel) {
        mChooseAreaView = view;
        mChooseAreaModel = chooseAreaModel;
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
        mChooseAreaView.setTitle(mChooseAreaModel.getString(R.string.china));
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
        if (!isFromWeatherActivity && checkCountySelected()) {
            mChooseAreaView.goToWeatherActivity();
        } else {
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
        LogUtil.d(TAG, "Net Error");
    }
}
