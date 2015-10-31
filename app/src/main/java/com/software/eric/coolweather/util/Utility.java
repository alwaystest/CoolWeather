package com.software.eric.coolweather.util;

import android.text.TextUtils;

import com.software.eric.coolweather.db.CoolWeatherDB;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;

/**
 * Created by Mzz on 2015/10/30.
 */
public class Utility {
    public synchronized  static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvince = response.split(",");
            if(allProvince != null && allProvince.length > 0){
                for(String p : allProvince){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized  static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCity = response.split(",");
            if(allCity != null && allCity.length > 0){
                for(String c : allCity){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    LogUtil.i("CoolWeather",city.toString());
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized  static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId){
        if(!TextUtils.isEmpty(response)){
            String[] allCounty = response.split(",");
            if(allCounty != null && allCounty.length > 0){
                for(String c : allCounty){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
}
