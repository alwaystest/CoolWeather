package com.software.eric.coolweather.util;

/**
 * Created by Mzz on 2015/10/29.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
