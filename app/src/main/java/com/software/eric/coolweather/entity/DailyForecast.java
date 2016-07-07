package com.software.eric.coolweather.entity;

/**
 * Created by Mzz on 2016/2/10.
 */
public class DailyForecast {

    /**
     * date : 2015-07-15
     * astro : {"sr":"04:40","ss":"19:19"}
     * cond : {"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"}
     * hum : 48
     * pcpn : 0.0
     * pop : 0
     * pres : 1005
     * tmp : {"max":"33","min":"24"}
     * vis : 10
     * wind : {"deg":"192","dir":"东南风","sc":"4-5","spd":"11"}
     */

    private String date;
    /**
     * sr : 04:40
     * ss : 19:19
     */

    private AstroEntity astro;
    /**
     * code_d : 100
     * code_n : 101
     * txt_d : 晴
     * txt_n : 多云
     */

    private Cond cond;
    private String hum;
    private String pcpn;
    private String pop;
    private String pres;
    /**
     * max : 33
     * min : 24
     */

    private TmpEntity tmp;
    private String vis;
    /**
     * deg : 192
     * dir : 东南风
     * sc : 4-5
     * spd : 11
     */

    private Wind wind;

    public void setDate(String date) {
        this.date = date;
    }

    public void setAstro(AstroEntity astro) {
        this.astro = astro;
    }

    public void setCond(Cond cond) {
        this.cond = cond;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public void setTmp(TmpEntity tmp) {
        this.tmp = tmp;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getDate() {
        return date;
    }

    public AstroEntity getAstro() {
        return astro;
    }

    public Cond getCond() {
        return cond;
    }

    public String getHum() {
        return hum;
    }

    public String getPcpn() {
        return pcpn;
    }

    public String getPop() {
        return pop;
    }

    public String getPres() {
        return pres;
    }

    public TmpEntity getTmp() {
        return tmp;
    }

    public String getVis() {
        return vis;
    }

    public Wind getWind() {
        return wind;
    }

    public static class AstroEntity {
        private String sr;
        private String ss;

        public void setSr(String sr) {
            this.sr = sr;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }

        public String getSr() {
            return sr;
        }

        public String getSs() {
            return ss;
        }
    }


    public static class TmpEntity {
        private int max;
        private int min;

        public void setMax(int max) {
            this.max = max;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public int getMin() {
            return min;
        }
    }
}
