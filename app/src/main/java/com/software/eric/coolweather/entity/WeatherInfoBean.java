package com.software.eric.coolweather.entity;

/**
 * Created by Mzz on 2016/2/7.
 */
public class WeatherInfoBean {
    private Basic basic;
    private String status;
    private Aqi aqi;
    private Alarms[] alarms;
    private Now now;
    private DailyForecast[] daily_forecast;
    private HourlyForecast[] hourly_forecast;
    private Suggestion suggestion;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aqi getAqi() {
        return aqi;
    }

    public void setAqi(Aqi aqi) {
        this.aqi = aqi;
    }

    public Alarms[] getAlarms() {
        return alarms;
    }

    public void setAlarms(Alarms[] alarms) {
        this.alarms = alarms;
    }

    public Now getNow() {
        return now;
    }

    public void setNow(Now now) {
        this.now = now;
    }

    public DailyForecast[] getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(DailyForecast[] daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public HourlyForecast[] getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(HourlyForecast[] hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    public Suggestion getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(Suggestion suggestion) {
        this.suggestion = suggestion;
    }
}
