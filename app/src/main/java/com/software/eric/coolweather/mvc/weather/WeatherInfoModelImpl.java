package com.software.eric.coolweather.mvc.weather;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.software.eric.coolweather.constants.IConst;
import com.software.eric.coolweather.constants.Key;
import com.software.eric.coolweather.entity.County;
import com.software.eric.coolweather.entity.WeatherInfo;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.MyApplication;

/**
 * Created by Mzz on 2016/2/10.
 */
public class WeatherInfoModelImpl implements WeatherContract.IWeatherInfoModel {
    static final int BY_NAME = 0;
    private static final int BY_CODE = 1;
    private final Gson mGson;

    public WeatherInfoModelImpl(Gson gson) {
        mGson = gson;
    }

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
    public void saveWeatherInfo(WeatherInfo weatherInfoBean) {
        // TODO: 2016/2/20 find a better way
        String res = mGson.toJson(weatherInfoBean);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putString(IConst.WEATHER_INFO_V6, res);
        editor.apply();
    }

    @Override
    public WeatherInfo loadWeatherInfo() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String weatherInfo = preferences.getString(IConst.WEATHER_INFO_V6, null);
        if (TextUtils.isEmpty(weatherInfo)) {
            return null;
        }
        return mGson.fromJson(weatherInfo, WeatherInfo.class);
    }

    @Override
    public void queryWeatherAutoIp(final onLoadWeatherInfoListener listener) {
        String address = IConst.WEATHER + "?location=auto_ip" + "&key=" + Key.KEY;
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                WeatherInfo weatherInfo = mGson.fromJson(response, WeatherInfo.class);
                saveWeatherInfo(weatherInfo);
                if (listener != null) {
                    listener.onFinish(weatherInfo);
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

    @Override
    public void queryFromServer(final String address, final int type, final onLoadWeatherInfoListener listener) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if (type == BY_NAME || type == BY_CODE) {
                    WeatherInfo weatherInfo = mGson.fromJson(response, WeatherInfo.class);
                    saveWeatherInfo(weatherInfo);
                    if (listener != null) {
                        listener.onFinish(weatherInfo);
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
        void onFinish(WeatherInfo weatherInfo);

        void onError();
    }
}
