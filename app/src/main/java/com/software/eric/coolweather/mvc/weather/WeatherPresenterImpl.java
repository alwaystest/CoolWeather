package com.software.eric.coolweather.mvc.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.software.eric.coolweather.constants.ExtraConstant;
import com.software.eric.coolweather.entity.HeWeather;
import com.software.eric.coolweather.entity.WeatherInfo;
import com.software.eric.coolweather.service.UpdateWeatherInfoService;
import com.software.eric.coolweather.util.MyApplication;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import javax.inject.Inject;

/**
 * Created by Mzz on 2016/2/7.
 */
public class WeatherPresenterImpl {

    WeatherContract.IWeatherView mWeatherView;
    WeatherContract.IWeatherInfoModel mWeatherInfoModel;
    CompositeDisposable mCompositeDisposable;

    @Inject
    public WeatherPresenterImpl(WeatherContract.IWeatherView view, WeatherContract.IWeatherInfoModel weatherInfoModel) {
        mWeatherInfoModel = weatherInfoModel;
        mWeatherView = view;
        mCompositeDisposable = new CompositeDisposable();
    }


    /**
     * query weather.
     * load from prefs or internet
     * @param isRefresh if is swipe refresh.
     */
    public void queryWeather(boolean isRefresh) {
        WeatherInfo weatherInfo = mWeatherInfoModel.loadWeatherInfo();
        //if is not swipe refresh, load weatherInfo from prefs.
        if (!isRefresh && weatherInfo != null && weatherInfo.getHeWeather().size() >= 1) {
            mWeatherView.showWeather(weatherInfo.getHeWeather().get(0));
            mWeatherView.setRefreshing(false);
            return;
        }
        //else load from Internet.
        queryWeatherAutoIp();
    }


    public void setAutoUpdateService() {
        Context context = MyApplication.getContext();
        //set alarm , update per 24H if not set
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int autoUpdateTime = prefs.getInt(ExtraConstant.AUTO_UPDATE_TIME, ExtraConstant.DEFAULT_AUTO_UPDATE_TIME);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long nowTime = System.currentTimeMillis();
        long time = autoUpdateTime * 60 * 60 * 1000 + nowTime;
        Intent i = new Intent(context, UpdateWeatherInfoService.class);
        //TODO: change 4th param
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.set(AlarmManager.RTC, time, pi);
    }

    public void ifGoChooseArea() {
        if (!mWeatherInfoModel.checkCountySelected()) {
            mWeatherView.goChooseArea();
        } else {
            mWeatherView.initView();
        }
    }

    private void queryWeatherAutoIp() {
        mWeatherView.showSyncing();
        mWeatherInfoModel.queryWeatherAutoIp(new WeatherInfoModelImpl.onLoadWeatherInfoListener() {
            @Override
            public void onFinish(WeatherInfo weatherInfo) {
                if (weatherInfo.getHeWeather().size() < 1) {
                    onError();
                    return;
                }
                mCompositeDisposable.add(Observable.just(weatherInfo.getHeWeather().get(0))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<HeWeather>() {
                            @Override
                            public void accept(HeWeather weatherInfo) {
                                mWeatherView.setRefreshing(false);
                                mWeatherView.showWeather(weatherInfo);
                            }
                        }));
            }

            @Override
            public void onError() {
                mWeatherView.showFailed();
                mWeatherView.setRefreshing(false);
            }
        });
    }
}
