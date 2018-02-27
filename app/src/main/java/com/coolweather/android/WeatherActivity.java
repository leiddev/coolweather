package com.coolweather.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coolweather.android.gson.Forecast;
import com.coolweather.android.gson.Weather;
import com.coolweather.android.service.AutoUpdateService;
import com.coolweather.android.util.HttpUtil;
import com.coolweather.android.util.TimeUtility;
import com.coolweather.android.util.Utility;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.coolweather.android.R.id.precipitation;

public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    private Button navButton;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private TextView feltTemperatureText;
    private TextView humidityText;
    private TextView precipitationText;
    private TextView pressureText;
    private TextView visibilityText;
    private TextView windInfoText;
    private LinearLayout forecastLayout;
    private TextView airQualityText;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView pm10Text;
    private TextView no2Text;
    private TextView so2Text;
    private TextView coText;
    private TextView o3Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private TextView airText;
    private TextView dressingText;
    private TextView influenzaText;
    private TextView travelText;
    private TextView ultravioletrayText;
    private ImageView bingPicImg;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);

        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        feltTemperatureText = (TextView) findViewById(R.id.felt_temperature);
        humidityText = (TextView) findViewById(R.id.humidity);
        precipitationText  = (TextView) findViewById(precipitation);
        pressureText  = (TextView) findViewById(R.id.pressure);
        visibilityText = (TextView) findViewById(R.id.visibility);
        windInfoText  = (TextView) findViewById(R.id.wind_info);

        airQualityText = (TextView) findViewById(R.id.air_quality_text);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        pm10Text = (TextView) findViewById(R.id.pm10_text);
        no2Text = (TextView) findViewById(R.id.no2_text);
        so2Text = (TextView) findViewById(R.id.so2_text);
        coText = (TextView) findViewById(R.id.co_text);
        o3Text = (TextView) findViewById(R.id.o3_text);

        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        airText = (TextView) findViewById(R.id.air_text);
        dressingText = (TextView) findViewById(R.id.dressing_text);
        influenzaText = (TextView) findViewById(R.id.influenza_text);
        travelText = (TextView) findViewById(R.id.travel_text);
        ultravioletrayText = (TextView) findViewById(R.id.ultravioletray_text);

        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather", null);
        if (weatherString != null) {
            Weather weather = Utility.handleWeatherResponse(weatherString);
            if (weather != null) {
                mWeatherId = weather.basic.weatherId;

                String updateTime = weather.basic.update.updateTime + ":00";
                String oneHourBeforeTime = TimeUtility.getLongTime(-1);
                if (TimeUtility.isDateOneBigger(updateTime, oneHourBeforeTime)) {
                    showWeatherInfo(weather);
                } else {    // 距离上次更新超过1小时
                    requestWeather(mWeatherId);
                }
            } else {
                mWeatherId = getIntent().getStringExtra("weather_id");
                weatherLayout.setVisibility(View.INVISIBLE);
                requestWeather(mWeatherId);
            }
        } else {
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        String bingPic = prefs.getString("bing_pic", null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else {
            loadBingPic();
        }
    }

    public void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=dd241860551e4628b999d0cb4362d9d4";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null & weather.status != null & "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });

        loadBingPic();
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    public void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;
        String feltTemperature = "体感温度 " + weather.now.feltAirTemperature + "°C";
        String humidity = "相对湿度 " + weather.now.humidity + "%";
        String precipitation = "降水  " + weather.now.precipitation + "mm";
        String pressure = "气压 " + weather.now.pressure + "hpa";
        String visibility = "能见度 " +weather.now.visibility + "km";
        String windInfo = weather.now.wind.direction + " "
                + weather.now.wind.scale + "级 "
                + weather.now.wind.speed + "km/h";

        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        loadWeatherIcon(weather, weatherIcon);
        feltTemperatureText.setText(feltTemperature);
        humidityText.setText(humidity);
        precipitationText.setText(precipitation);
        pressureText.setText(pressure);
        visibilityText.setText(visibility);
        windInfoText.setText(windInfo);

        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            ImageView infoImage = (ImageView) view.findViewById(R.id.info_image);
            TextView precipitationProbText = (TextView) view.findViewById(R.id.precipitation_prob);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(forecast.date + "\n" + getDateHint(forecast.date));
            infoText.setText(forecast.more.info);
            loadWeatherIcon(forecast, infoImage);
            precipitationProbText.setText(forecast.precipitationProbability + "%降水");
            maxText.setText(forecast.temperature.max + "°C");
            minText.setText(forecast.temperature.min + "°C");
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            airQualityText.setText(weather.aqi.city.quality);
            if (airQualityText.getText().length() == 1) {
                airQualityText.setTextSize(35);
            } else {
                airQualityText.setTextSize(25);
            }
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
            pm10Text.setText(weather.aqi.city.pm10);
            no2Text.setText(weather.aqi.city.no2);
            so2Text.setText(weather.aqi.city.so2);
            coText.setText(weather.aqi.city.co);
            o3Text.setText(weather.aqi.city.o3);
        }

        String comfort = "舒适度：" + weather.suggestion.comfort.brief + "\n" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.brief + "\n" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.brief + "\n" + weather.suggestion.sport.info;
        String air = "空气质量：" + weather.suggestion.air.brief + "\n" + weather.suggestion.air.info;
        String dressing = "穿衣建议：" + weather.suggestion.dressing.brief + "\n" + weather.suggestion.dressing.info;
        String influenza = "感冒指数：" + weather.suggestion.influenza.brief + "\n" + weather.suggestion.influenza.info;
        String travel = "旅游建议：" + weather.suggestion.travel.brief + "\n" + weather.suggestion.travel.info;
        String ultravioletray = "紫外线强度：" + weather.suggestion.ultravioletray.brief + "\n" + weather.suggestion.ultravioletray.info;

        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        airText.setText(air);
        dressingText.setText(dressing);
        influenzaText.setText(influenza);
        travelText.setText(travel);
        ultravioletrayText.setText(ultravioletray);

        weatherLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    private void loadWeatherIcon(Weather weather, ImageView imageView) {
        boolean nightFlag = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = sdf.parse(weather.basic.update.updateTime);
            Calendar cd = sdf.getCalendar();
            if (cd.get(Calendar.HOUR_OF_DAY) >= 18 || cd.get(Calendar.HOUR_OF_DAY) < 6) {
                nightFlag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String weatherCode = weather.now.more.infoCode;
        if (nightFlag) {
            try {
                int infoCode = Integer.parseInt(weatherCode);
                if (infoCode == 100 || infoCode == 103 || infoCode == 104 || infoCode == 300
                        || infoCode == 301 || infoCode == 406 || infoCode == 407) {
                    weatherCode += "n";
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        int weatherCodeResId = Utility.getResourceId("weather_icon_" + weatherCode, R.drawable.class);
        Glide.with(this)
                .load(weatherCodeResId)
                .bitmapTransform(new ColorFilterTransformation(this, 0xFFFFFFFF))
                .into(imageView);
    }

    private void loadWeatherIcon(Forecast forecast, ImageView imageView) {
        String weatherCode = forecast.more.infoCode;
        int weatherCodeResId = Utility.getResourceId("weather_icon_" + weatherCode, R.drawable.class);
        Glide.with(this)
                .load(weatherCodeResId)
                .bitmapTransform(new ColorFilterTransformation(this, 0xFFFFFFFF))
                .into(imageView);
    }

    private String getDateHint(String dateString) {
        String hintString = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = sdf.parse(dateString);
            long delta = date.getTime() / (24 * 3600 * 1000) - System.currentTimeMillis() / (24 * 3600 * 1000);
            switch ((int)delta) {
                case -2:
                    hintString = "前天";
                    break;
                case -1:
                    hintString = "昨天";
                    break;
                case 0:
                    hintString = "今天";
                    break;
                case 1:
                    hintString = "明天";
                    break;
                case 2:
                    hintString = "后天";
                    break;
                default:
                    hintString = TimeUtility.getWeekday(date);
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return hintString;
    }
}
