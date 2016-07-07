package com.software.eric.coolweather.mvc.weather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.software.eric.coolweather.constants.IConst;
import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.WeatherInfoBean;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.MyApplication;
import com.software.eric.coolweather.util.Utility;

/**
 * Created by Mzz on 2016/2/10.
 */
public class WeatherInfoModelImpl implements IWeatherInfoModel {
    public static final int BY_NAME = 0;
    public static final int BY_CODE = 1;

    @Override
    public boolean checkCountySelected() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return !TextUtils.isEmpty(preferences.getString(IConst.COUNTY_NAME, null));
    }

    @Override
    public County loadCounty() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String countyName = preferences.getString(IConst.COUNTY_NAME, "北京");
        County county = new County();
        county.setName(countyName);
        return county;
    }

    @Override
    public void saveWeatherInfo(WeatherInfoBean weatherInfoBean) {
        // TODO: 2016/2/20 find a better way
        String res = JSON.toJSONString(weatherInfoBean);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putString(IConst.WEATHER_INFO, res);
        editor.apply();
    }

    @Override
    public WeatherInfoBean loadWeatherInfo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String weatherInfo = preferences.getString(IConst.WEATHER_INFO, null);
        if (TextUtils.isEmpty(weatherInfo)) {
            return null;
        }
        return JSON.parseObject(weatherInfo, WeatherInfoBean.class);
    }

    @Override
    public void queryFromServer(final String address, final int type, final onLoadWeatherInfoListener listener) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (type == BY_NAME || type == BY_CODE) {
                    WeatherInfoBean weatherInfoBean = Utility.handleWeatherResponse(response);
                    saveWeatherInfo(weatherInfoBean);
                    if (listener != null) {
                        listener.onFinish(weatherInfoBean);
                    }

                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                LogUtil.e("onError", e.toString());
                if (listener != null) {
                    listener.onError();
                }
            }
        });
    }

    public interface onLoadWeatherInfoListener {
        void onFinish(WeatherInfoBean weatherInfo);

        void onError();
    }
}
