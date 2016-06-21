package com.software.eric.coolweather.activity.choosearea.view;

import com.software.eric.coolweather.model.Address;

import java.util.List;

/**
 * Created by Mzz on 2016/2/15.
 */
public interface IChooseAreaView {
    void showProgressDialog();

    void closeProgressDialog();

    void setList(List<? extends Address> addressList);

    void setTitle(String title);

    void setCurrentLevel(int level);

    void setSelectedAddress(Address selectedAddress);

    int getCurrentLevel();

    void goToWeatherActivity();
}
