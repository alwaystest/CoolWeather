package com.software.eric.coolweather.model;

/**
 * Created by Mzz on 2015/10/30.
 */
public class City extends Address{

    private int provinceId;

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", cityName='" + name + '\'' +
                ", cityCode='" + code + '\'' +
                ", provinceId=" + provinceId +
                '}';
    }
}
