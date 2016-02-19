package com.software.eric.coolweather.activity.choosearea.presenter;

import com.software.eric.coolweather.activity.choosearea.model.ChooseAreaModelImpl;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;

/**
 * Created by Mzz on 2016/2/15.
 */
public interface IChooseAreaPresenter {
    void saveSelectedCounty(County county);
    boolean checkCountySelected();
    void queryProvinces();
    void queryCities(Province selectedProvince);
    void queryCounties(City selectedCity);
}
