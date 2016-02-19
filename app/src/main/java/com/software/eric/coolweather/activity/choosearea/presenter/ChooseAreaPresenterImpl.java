package com.software.eric.coolweather.activity.choosearea.presenter;

import com.software.eric.coolweather.R;
import com.software.eric.coolweather.activity.choosearea.model.ChooseAreaModelImpl;
import com.software.eric.coolweather.activity.choosearea.model.IChooseAreaModel;
import com.software.eric.coolweather.activity.choosearea.view.ChooseAreaActivity;
import com.software.eric.coolweather.activity.choosearea.view.IChooseAreaView;
import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;
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
        mChooseAreaView.closeProgressDialog();
    }

    @Override
    public void queryCities(Province selectedProvince) {
        mChooseAreaView.showProgressDialog();
        mChooseAreaModel.queryCities(selectedProvince, this);
        mChooseAreaView.setTitle(selectedProvince.getName());
        mChooseAreaView.setCurrentLevel(ChooseAreaActivity.LEVEL_CITY);
        mChooseAreaView.closeProgressDialog();
    }

    @Override
    public void queryCounties(City selectedCity) {
        mChooseAreaView.showProgressDialog();
        mChooseAreaModel.queryCounties(selectedCity,this);
        mChooseAreaView.setTitle(selectedCity.getName());
        mChooseAreaView.setCurrentLevel(ChooseAreaActivity.LEVEL_COUNTY);
        mChooseAreaView.closeProgressDialog();
    }

    @Override
    public void onSuccess(List<? extends Address> list) {
        mChooseAreaView.setList(list);
    }

    @Override
    public void onFailure() {

    }
}
