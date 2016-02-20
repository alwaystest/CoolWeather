package com.software.eric.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.software.eric.coolweather.beans.china.WeatherInfoBean;
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
    private static String TAG = "Utility";

    public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvince = response.split(",");
            if (allProvince != null && allProvince.length > 0) {
                for (String p : allProvince) {
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setCode(array[0]);
                    province.setName(array[1]);
                    coolWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCity = response.split(",");
            if (allCity != null && allCity.length > 0) {
                for (String c : allCity) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCode(array[0]);
                    city.setName(array[1]);
                    city.setProvinceId(provinceId);
                    coolWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounty = response.split(",");
            if (allCounty != null && allCounty.length > 0) {
                for (String c : allCounty) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCode(array[0]);
                    county.setName(array[1]);
                    county.setCityId(cityId);
                    coolWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static WeatherInfoBean handleWeatherResponse(String response) {
        WeatherInfoBean weatherInfoBean = null;
        try {
            LogUtil.i("response", response);
            JSONObject jsonObject = new JSONObject(response);
            JSONObject root = jsonObject.getJSONArray("HeWeather data service 3.0").getJSONObject(0);
            if ("ok".equals(root.getString("status"))) {
                String tmp = root.toString();
                weatherInfoBean = JSON.parseObject(tmp, WeatherInfoBean.class);
                /*String cityName = root.getJSONObject("basic").getString("city");
                String weatherCode = root.getJSONObject("basic").getString("id");
                JSONObject todayInfo = root.getJSONArray("daily_forecast").getJSONObject(0);
                JSONObject todayTemp = todayInfo.getJSONObject("tmp");
                String minTemp = todayTemp.getString("min");
                String maxTemp = todayTemp.getString("max");
                //天气状况
                JSONObject cond = todayInfo.getJSONObject("cond");
                String weatherDesp = cond.getString("txt_d");
                String publishTime = root.getJSONObject("basic").getJSONObject("update").getString("loc");*/
//                saveWeatherInfo(context, cityName, weatherCode, minTemp, maxTemp, weatherDesp, publishTime);
            } else {
//                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent("City Not Supported"));
                LogUtil.e("response", "city not supported");
            }
        } catch (JSONException e) {
            LogUtil.e("CoolWeather", e.toString());
        }
        return weatherInfoBean;
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月d日HH:mm", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", simpleDateFormat.format(new Date()));
        editor.apply();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
