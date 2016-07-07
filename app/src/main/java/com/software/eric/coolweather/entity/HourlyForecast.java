package com.software.eric.coolweather.entity;

/**
 * Created by Mzz on 2016/2/10.
 */
public class HourlyForecast {

    /**
     * date : 2015-07-15 10:00
     * hum : 51
     * pop : 0
     * pres : 1006
     * tmp : 32
     * wind : {"deg":"127","dir":"东南风","sc":"微风","spd":"4"}
     */

    private String date;
    private String hum;
    private String pop;
    private String pres;
    private String tmp;
    private Wind wind;

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getDate() {
        return date;
    }

    public String getHum() {
        return hum;
    }

    public String getPop() {
        return pop;
    }

    public String getPres() {
        return pres;
    }

    public String getTmp() {
        return tmp;
    }
}
