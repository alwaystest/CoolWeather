package com.software.eric.coolweather.mvc.choosearea;

import com.software.eric.coolweather.entity.Address;

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

    void initView();
}
