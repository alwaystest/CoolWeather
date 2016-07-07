package com.software.eric.coolweather.entity;

/**
 * Created by Mzz on 2016/2/10.
 */
public class Basic {

    /**
     * city : 大连
     * cnty : 中国
     * id : CN101070201
     * lat : 38.944000
     * lon : 121.576000
     * update : {"loc":"2015-07-15 10:43","utc":"2015-07-15 02:46:14"}
     */

    private String city;
    private String cnty;
    private String id;
    private String lat;
    private String lon;
    private Update update;

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public String getCnty() {
        return cnty;
    }

    public String getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
