package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ld-1 on 2018/2/11.
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("fl")
    public String feltAirTemperature;

    @SerializedName("hum")
    public String humidity;

    @SerializedName("pcpn")
    public String precipitation;

    @SerializedName("pres")
    public String pressure;

    @SerializedName("vis")
    public String visibility;

    @SerializedName("cond")
    public More more;

    @SerializedName("wind_deg")
    public String windDegree;

    @SerializedName("wind_dir")
    public String windDirection;

    @SerializedName("wind_sc")
    public String windScale;

    @SerializedName("wind_spd")
    public String windSpeed;

    public Wind wind;

    public class More{
        @SerializedName("txt")
        public String info;

        @SerializedName("code")
        public String infoCode;
    }

    public class Wind{
        @SerializedName("deg")
        public String degree;

        @SerializedName("dir")
        public String direction;

        @SerializedName("sc")
        public String scale;

        @SerializedName("spd")
        public String speed;
    }
}
