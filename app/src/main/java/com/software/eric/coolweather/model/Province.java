package com.software.eric.coolweather.model;

/**
 * Created by Mzz on 2015/10/30.
 */
public class Province extends Address{

    @Override
    public String toString() {
        return "Province{" +
                "id=" + id +
                ", provinceName='" + name + '\'' +
                ", provinceCode='" + code + '\'' +
                '}';
    }
}
