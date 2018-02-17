package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ld-1 on 2018/2/11.
 */

public class Forecast {

    public String date;

    @SerializedName("hum")
    public String humidity;

    @SerializedName("pcpn")
    public String precipitation;

    @SerializedName("pop")
    public String precipitationProbability;

    @SerializedName("pres")
    public String pressure;

    @SerializedName("uv")
    public String ultravioletray;

    @SerializedName("vis")
    public String visibility;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    @SerializedName("astro")
    public Astronomy astronomy;

    public Wind wind;

    public class Temperature{
        public String max;
        public String min;
    }

    public class More{
        @SerializedName("txt_d")
        public String info;

        @SerializedName("txt_n")
        public String nightInfo;

        @SerializedName("code_d")
        public String infoCode;

        @SerializedName("code_n")
        public String nightInfoCode;
    }

    public class Astronomy{
        @SerializedName("mr")
        public String moonrise;

        @SerializedName("ms")
        public String moonset;

        @SerializedName("sr")
        public String sunrise;

        @SerializedName("ss")
        public String sunset;
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
