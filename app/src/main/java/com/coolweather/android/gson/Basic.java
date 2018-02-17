package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ld-1 on 2018/2/11.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("cnty")
    public String countryName;

    @SerializedName("id")
    public String weatherId;

    @SerializedName("lat")
    public String cityLatitude;

    @SerializedName("lon")
    public String cityLongitude;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;

        @SerializedName("utc")
        public String updateTimeUTC;
    }
}
