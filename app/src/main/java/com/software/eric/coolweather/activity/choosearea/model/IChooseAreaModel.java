package com.software.eric.coolweather.activity.choosearea.model;

import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;

/**
 * Created by Mzz on 2016/2/15.
 */
public interface IChooseAreaModel {
    void saveSelectedCounty(County county);
    boolean checkCountySelected();
    void queryProvinces(ChooseAreaModelImpl.OnLoadListListener listener);
    void queryCities(Province selectedProvince,ChooseAreaModelImpl.OnLoadListListener listener);
    void queryCounties(City selectedCity,ChooseAreaModelImpl.OnLoadListListener listener);
}
