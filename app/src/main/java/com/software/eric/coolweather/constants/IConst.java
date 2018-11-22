package com.software.eric.coolweather.constants;

/**
 * Created by Mzz on 2016/2/11.
 */
public interface IConst {
    String SERVER_ROOT = "https://free-api.heweather.com";
    String API_ROOT = SERVER_ROOT + "/s6";
    String WEATHER = API_ROOT + "/weather";
    String DAILY_FORECAST = WEATHER + "/forecast";
    String COUNTY_NAME = "county_name";
    /**
     * weather info key of olg version, should be removed
     */
    @Deprecated
    String WEATHER_INFO = "weather_info";
    String WEATHER_INFO_V6 = "weather_info_v6";
}
