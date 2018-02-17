package com.coolweather.android.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ld-1 on 2018/2/11.
 */

public class Suggestion {

    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public Air air;

    @SerializedName("drsg")
    public Dressing dressing;

    @SerializedName("flu")
    public Influenza influenza;

    @SerializedName("trav")
    public Travel travel;

    @SerializedName("uv")
    public Ultravioletray ultravioletray;

    public class Comfort{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class Sport{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class Air{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class Dressing{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class Influenza{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class Travel{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }

    public class Ultravioletray{
        @SerializedName("txt")
        public String info;

        @SerializedName("brf")
        public String brief;
    }
}
