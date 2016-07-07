package com.software.eric.coolweather.entity;

/**
 * Created by Mzz on 2016/2/10.
 */
public class Update {

    /**
     * loc : 2015-07-15 10:43
     * utc : 2015-07-15 02:46:14
     */

    private String loc;
    private String utc;

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public String getLoc() {
        return loc;
    }

    public String getUtc() {
        return utc;
    }
}
