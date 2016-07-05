package com.software.eric.coolweather.activity.choosearea.model;

import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.County;

/**
 * Created by Mzz on 2016/2/15.
 */
public interface IChooseAreaModel {
    void saveSelectedCounty(County county);
    boolean checkCountySelected();
    void queryProvinces(ChooseAreaModelImpl.OnLoadListListener listener);
    void queryCities(Address selectedProvince,ChooseAreaModelImpl.OnLoadListListener listener);
    void queryCounties(Address selectedCity,ChooseAreaModelImpl.OnLoadListListener listener);
    String getString(int id);
}
