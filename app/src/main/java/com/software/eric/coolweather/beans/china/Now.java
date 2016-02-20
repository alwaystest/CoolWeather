package com.software.eric.coolweather.beans.china;

/**
 * Created by Mzz on 2016/2/10.
 */
public class Now {

    /**
     * code : 100
     * txt : 晴
     */

    private Cond cond;
    /**
     * cond : {"code":"100","txt":"晴"}
     * fl : 33
     * hum : 28
     * pcpn : 0
     * pres : 1005
     * tmp : 32
     * vis : 10
     * wind : {"deg":"350","dir":"东北风","sc":"4-5","spd":"11"}
     */

    private String fl;
    private String hum;
    private String pcpn;
    private String pres;
    private String tmp;
    private String vis;
    /**
     * deg : 350
     * dir : 东北风
     * sc : 4-5
     * spd : 11
     */

    private Wind wind;

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Cond getCond() {
        return cond;
    }

    public String getFl() {
        return fl;
    }

    public String getHum() {
        return hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public String getPres() {
        return pres;
    }

    public String getTmp() {
        return tmp;
    }

    public String getVis() {
        return vis;
    }

    public Wind getWind() {
        return wind;
    }

}
