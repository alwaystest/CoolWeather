package com.software.eric.coolweather.activity.choosearea.presenter;

import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.County;

import java.util.List;

/**
 * Created by Mzz on 2016/2/15.
 */
public interface IChooseAreaPresenter {
    void saveSelectedCounty(County county);
    boolean checkCountySelected();
    void queryProvinces();
    void queryCities(Address selectedProvince);
    void queryCounties(Address selectedCity);
    void checkIfGoToWeatherActivity(boolean isFromWeatherActivity);
    void onListItemClicked(int currentLevel, Address selectedAddress);
}
