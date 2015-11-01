package com.software.eric.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.software.eric.coolweather.db.CoolWeatherDB;
import com.software.eric.coolweather.model.City;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    public synchronized static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            //in response, temp2 is lower than temp1.
            String temp1 = weatherInfo.getString("temp2");
            String temp2 = weatherInfo.getString("temp1");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            LogUtil.e("CoolWeather",e.toString());
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日hh:mm", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", simpleDateFormat.format(new Date()));
        editor.commit();
    }
}
