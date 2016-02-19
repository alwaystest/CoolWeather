package com.software.eric.coolweather.model;

/**
 * Created by Mzz on 2015/10/30.
 */
public class County extends Address{
    private int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
