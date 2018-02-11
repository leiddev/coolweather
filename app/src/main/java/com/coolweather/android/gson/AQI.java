package com.coolweather.android.gson;

/**
 * Created by ld-1 on 2018/2/11.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
