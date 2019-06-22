package com.software.eric.coolweather.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import com.software.eric.coolweather.constants.ExtraConstant;
import com.software.eric.coolweather.entity.WeatherInfo;
import com.software.eric.coolweather.mvc.key.KeyContract;
import com.software.eric.coolweather.mvc.key.KeySettingRepo;
import com.software.eric.coolweather.mvc.weather.WeatherContract;
import com.software.eric.coolweather.mvc.weather.WeatherInfoModelImpl;
import com.software.eric.coolweather.util.LogUtil;

/**
 * Created by Mzz on 2015/11/1.
 */
public class UpdateWeatherInfoService extends IntentService {
    private static final String TAG = "UpdateWeatherInfoService";
    WeatherContract.IWeatherInfoModel mWeatherInfoModel;
    KeyContract.Repo mKeyRepo;

    public UpdateWeatherInfoService() {
        super("UpdateWeatherInfoService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // FIXME: 2018/11/21 Injection Singleton
        mWeatherInfoModel = new WeatherInfoModelImpl(new Gson());
        mKeyRepo = new KeySettingRepo();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int autoUpdateTime = prefs.getInt(ExtraConstant.AUTO_UPDATE_TIME, 24);
        LogUtil.i(TAG, String.valueOf(autoUpdateTime));
        String apiKey = mKeyRepo.getApiKey();
        if (apiKey.isEmpty()) {
            return;
        }
        mWeatherInfoModel.queryWeatherAutoIp(apiKey, new WeatherInfoModelImpl.onLoadWeatherInfoListener() {
            @Override
            public void onFinish(WeatherInfo weatherInfo) {

            }

            @Override
            public void onError() {
                LogUtil.e(TAG, "Update weather error");
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
