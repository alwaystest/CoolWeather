package com.software.eric.coolweather.beans.china;

/**
 * Created by Mzz on 2016/2/10.
 */
public class Wind {
    private String deg;
    private String dir;
    private String sc;
    private String spd;

    public void setDeg(String deg) {
        this.deg = deg;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    public void setSpd(String spd) {
        this.spd = spd;
    }

    public String getDeg() {
        return deg;
    }

    public String getDir() {
        return dir;
    }

    public String getSc() {
        return sc;
    }

    public String getSpd() {
        return spd;
    }
}