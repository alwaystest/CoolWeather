package com.software.eric.coolweather.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.software.eric.coolweather.constants.ExtraConstant;
import com.software.eric.coolweather.constants.Key;
import com.software.eric.coolweather.util.HttpCallbackListener;
import com.software.eric.coolweather.util.HttpUtil;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.Utility;

/**
 * Created by Mzz on 2015/11/1.
 */
public class UpdateWeatherInfoService extends IntentService {
    private static final String TAG = "UpdateWeatherInfoService";

    public UpdateWeatherInfoService() {
        super("UpdateWeatherInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int autoUpdateTime = prefs.getInt(ExtraConstant.AUTO_UPDATE_TIME, 24);
        LogUtil.i(TAG, String.valueOf(autoUpdateTime));
        String weatherCode = prefs.getString(ExtraConstant.WEATHER_CODE, "");
        String address = "http://api.heweather.com/x3/weather?cityid=" +
                weatherCode + "&key=" +
                Key.KEY;
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(response);
            }

            @Override
            public void onError(Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        });
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        long nowtime = System.currentTimeMillis();
        long time = autoUpdateTime * 60 * 60 * 1000 + nowtime;
        Intent i = new Intent(this, UpdateWeatherInfoService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.set(AlarmManager.RTC, time, pi);
    }
}
