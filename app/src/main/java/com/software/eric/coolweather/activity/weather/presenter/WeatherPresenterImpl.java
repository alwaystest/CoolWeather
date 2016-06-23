package com.software.eric.coolweather.activity.weather.presenter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.software.eric.coolweather.activity.weather.model.IWeatherInfoModel;
import com.software.eric.coolweather.activity.weather.model.WeatherInfoModelImpl;
import com.software.eric.coolweather.activity.weather.view.IWeatherView;
import com.software.eric.coolweather.beans.china.WeatherInfoBean;
import com.software.eric.coolweather.model.County;
import com.software.eric.coolweather.model.Key;
import com.software.eric.coolweather.service.UpdateWeatherInfoService;
import com.software.eric.coolweather.constants.IConst;
import com.software.eric.coolweather.util.LogUtil;
import com.software.eric.coolweather.util.MyApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Mzz on 2016/2/7.
 */
public class WeatherPresenterImpl implements IWeatherPresenter, WeatherInfoModelImpl.onLoadWeatherInfoListener {

    IWeatherView mWeatherView;
    IWeatherInfoModel mWeatherInfoModel;

    public WeatherPresenterImpl(IWeatherView weatherView) {
        this.mWeatherView = weatherView;
        mWeatherInfoModel = new WeatherInfoModelImpl();
    }

    @Override
    public void queryWeather(boolean isRefresh) {
        if (checkCountySelected()) {
            WeatherInfoBean weatherInfoBean = mWeatherInfoModel.loadWeatherInfo();
            if (!isRefresh && weatherInfoBean != null) {
                mWeatherView.showWeather(weatherInfoBean);
                mWeatherView.setRefreshing(false);
                return;
            }
            County county = mWeatherInfoModel.loadCounty();
            queryWeatherByName(county.getName());
        } else {
            mWeatherView.showFailed();
            mWeatherView.goChooseArea();
        }
    }


    @Override
    public void setAutoUpdateService() {
        Context context = MyApplication.getContext();
        //set alarm , update per 24H if not set
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int autoUpdateTime = prefs.getInt("auto_update_time", 24);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long nowTime = System.currentTimeMillis();
        long time = autoUpdateTime * 60 * 60 * 1000 + nowTime;
        Intent i = new Intent(context, UpdateWeatherInfoService.class);
        //TODO: change 4th param
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.set(AlarmManager.RTC, time, pi);
    }

    @Override
    public boolean checkCountySelected() {
        return mWeatherInfoModel.checkCountySelected();
    }

    @Override
    public void ifGoChooseArea() {
        if (mWeatherInfoModel.checkCountySelected()) {
            mWeatherView.goChooseArea();
        } else {
            mWeatherView.initView();
        }
    }

    @Override
    public void onFinish(WeatherInfoBean weatherInfo) {
        mWeatherView.showWeather(weatherInfo);
        mWeatherView.setRefreshing(false);
    }

    @Override
    public void onError() {
        mWeatherView.showFailed();
        mWeatherView.setRefreshing(false);
    }

    private void queryWeatherByCode(String countyCode) {
        if (countyCode == null) return;
        mWeatherView.showSyncing();
        String address = IConst.SERVER_ROOT + "?cityid=" +
                countyCode +
                "&key=" +
                Key.KEY;
        mWeatherInfoModel.queryFromServer(address, WeatherInfoModelImpl.BY_CODE, this);
    }

    private void queryWeatherByName(String countyName) {
        if (countyName == null) return;
        mWeatherView.showSyncing();
        String address = null;
        try {
            address = IConst.SERVER_ROOT + "?city=" +
                    URLEncoder.encode(countyName, "UTF-8") +
                    "&key=" +
                    Key.KEY;
        } catch (UnsupportedEncodingException e) {
            LogUtil.e("queryWeatherByName", e.toString());
        }
        mWeatherInfoModel.queryFromServer(address, WeatherInfoModelImpl.BY_NAME, this);
    }
}
