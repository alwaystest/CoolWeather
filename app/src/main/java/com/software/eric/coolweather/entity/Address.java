package com.software.eric.coolweather.entity;

/**
 * Created by Mzz on 2016/2/17.
 */
public class Address {
    protected int id;
    protected String name;
    protected String code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return name;
    }
}
