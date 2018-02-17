package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ld-1 on 2018/2/11.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
        public String pm10;
        public String no2;
        public String so2;
        public String co;
        public String o3;

        @SerializedName("qlty")
        public String quality;


    }
}
