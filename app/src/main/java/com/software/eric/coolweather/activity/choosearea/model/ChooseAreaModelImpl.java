package com.software.eric.coolweather.activity.choosearea.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.software.eric.coolweather.db.CoolWeatherDB;
import com.software.eric.coolweather.model.Address;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.IConst;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.MyApplication;
import com.software.eric.coolweather.util.Utility;

import java.util.List;

/**
 * Created by Mzz on 2016/2/15.
 */
public class ChooseAreaModelImpl implements IChooseAreaModel {
    public static final String TAG = "ChooseAreaModelImpl";
    public static final int PROVINCE = 0;
    public static final int CITY = 1;
    public static final int COUNTY = 2;

    @Override
    public void saveSelectedCounty(County county) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putString(IConst.COUNTY_NAME, county.getName());
        editor.apply();
    }

    @Override
    public boolean checkCountySelected() {
        String countyName = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).getString(IConst.COUNTY_NAME, null);
        return !TextUtils.isEmpty(countyName);
    }

    @Override
    public void queryProvinces(OnLoadListListener listener) {
        CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(MyApplication.getContext());
        List<Province> provinceList = coolWeatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            if (listener != null) {
                listener.onSuccess(provinceList);
            }
        } else {
            queryFromServer(null, PROVINCE, listener);
        }
    }

    @Override
    public void queryCities(Address selectedProvince, OnLoadListListener listener) {
        CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(MyApplication.getContext());
        List<City> cityList = coolWeatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            if (listener != null) {
                listener.onSuccess(cityList);
            }
        } else {
            queryFromServer(selectedProvince, CITY, listener);
        }
    }

    @Override
    public void queryCounties(Address selectedCity, OnLoadListListener listener) {
        CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(MyApplication.getContext());
        List<County> countyList = coolWeatherDB.loadCounty(selectedCity.getId());
        if (countyList.size() > 0) {
            if (listener != null) {
                listener.onSuccess(countyList);
            }
        } else {
            queryFromServer(selectedCity, COUNTY, listener);
        }
    }


    private void queryFromServer(final Address address, final int type, final OnLoadListListener listener) {
        String url;
        if (address != null && !TextUtils.isEmpty(address.getCode())) {
            url = "http://www.weather.com.cn/data/list3/city" +
                    address.getCode() + ".xml";
        } else {
            url = "http://www.weather.com.cn/data/list3/city.xml";
        }
        final CoolWeatherDB coolWeatherDB = CoolWeatherDB.getInstance(MyApplication.getContext());
        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                LogUtil.d(TAG, response);
                if (PROVINCE == type) {
                    result = Utility.handleProvinceResponse(coolWeatherDB, response);
                } else if (CITY == type) {
                    result = Utility.handleCityResponse(coolWeatherDB, response, address.getId());
                } else if (COUNTY == type) {
                    result = Utility.handleCountyResponse(coolWeatherDB, response, address.getId());
                }
                if (result) {
                    if (PROVINCE == type) {
                        queryProvinces(listener);
                    } else if (CITY == type) {
                        queryCities(address, listener);
                    } else if (COUNTY == type) {
                        queryCounties(address, listener);
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e(TAG, e.toString());
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }

    public interface OnLoadListListener {
        void onSuccess(List<? extends Address> list);

        void onFailure();
    }
}
