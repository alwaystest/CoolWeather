package com.software.eric.coolweather.mvc.choosearea;

import com.software.eric.coolweather.entity.Address;
import com.software.eric.coolweather.entity.County;

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
    void onListItemClicked(Address selectedAddress);
}
