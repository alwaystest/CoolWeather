package com.software.eric.coolweather.beans.china;

/**
 * Created by Mzz on 2016/2/10.
 */
public class Alarms {

    /**
     * level : 橙色
     * stat : 预警中
     * title : 辽宁省大连市气象台发布高温橙色预警
     * txt : 大连市气象台2015年07月14日13时31分发布高温橙色预警信号:预计14日下午至傍晚，旅顺口区局部最高气温将达到37℃以上,请注意防范。
     * type : 高温
     */

    private String level;
    private String stat;
    private String title;
    private String txt;
    private String type;

    public void setLevel(String level) {
        this.level = level;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public String getStat() {
        return stat;
    }

    public String getTitle() {
        return title;
    }

    public String getTxt() {
        return txt;
    }

    public String getType() {
        return type;
    }
}
