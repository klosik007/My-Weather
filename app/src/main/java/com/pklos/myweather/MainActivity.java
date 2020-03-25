package com.pklos.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static double _feelsLike = 0;
    private static String _city = "";
    private static double _temp = 0;
    private static long _timeFrom = 0L;
    private static String _mainWindDescription = "";
    private static String _advWindDescription = "";
    private static double _windSpeed = 0;
    private static int _windDegree = 0;
    private static double _tempMax = 0;
    private static double _tempMin = 0;
    private static long _sunrise = 0L;
    private static long _sunset = 0L;
    private static int _pressure = 0;
    private static int _humidity = 0;
    private static int _visibility = 0;
    private static List<Long> _dayTimes = new ArrayList<>();
    private static List<Double> _temps = new ArrayList<>();
    private static List<Double> _rainPreps = new ArrayList<>();
    private static List<Double> _snowPreps = new ArrayList<>();

    private String fiveDaysForecast = "http://api.openweathermap.org/data/2.5/forecast?id=7531002&appid=50768df1f9a4be14d70a612605801e5c";
    private String currentForecast = "http://api.openweathermap.org/data/2.5/weather?id=7531002&appid=50768df1f9a4be14d70a612605801e5c";

    public static String getCityName(){ return _city; }
    public static double getTemp(){ return _temp; }
    public static double getFeelsLike(){ return _feelsLike; }
    public static long getTimeFrom(){ return _timeFrom; }
    public static String getWindMainDescription() { return _mainWindDescription; }
    public static String getWindAdvDescription() { return _advWindDescription; }
    public static double getWindSpeed() { return _windSpeed; }
    public static int getWindDegree() { return _windDegree; }
    public static double getTempMax() { return _tempMax; }
    public static double getTempMin() { return _tempMin; }
    public static long getSunrise() { return _sunrise; }
    public static long getSunset() { return _sunset; }
    public static int getPressure() { return _pressure; }
    public static int getHumidity() { return _humidity; }
    public static int getVisibility() { return _visibility; }
    public static List<Long> getDtList() { return _dayTimes; }
    public static List<Double> getTempsList() { return _temps; }
    public static List<Double> getRainPrepsList() { return _rainPreps; }
    public static List<Double> getSnowPrepsList() { return _snowPreps; }

    public static void setCityName(String city) { _city = city; }
    public static void setTemperature(double temp) { _temp = temp; }
    public static void setFeelsLike(double feelsLike) { _feelsLike = feelsLike; }
    public static void setTimeFrom(long timeFrom) { _timeFrom = timeFrom; }
    public static void setWindMainDescription(String mainDescription) { _mainWindDescription = mainDescription; }
    public static void setWindAdvDescription(String advDescription) { _advWindDescription = advDescription; }
    public static void setWindSpeed(double windSpeed) { _windSpeed = windSpeed; }
    public static void setWindDegree(int windDegree) { _windDegree = windDegree; }
    public static void setTempMax(double tempMax) { _tempMax = tempMax; }
    public static void setTempMin(double tempMin) { _tempMin = tempMin; }
    public static void setSunrise(long sunrise) { _sunrise = sunrise; }
    public static void setSunset(long sunset) { _sunset = sunset; }
    public static void setPressure(int pressure) { _pressure = pressure; }
    public static void setHumidity(int humidity) { _humidity = humidity; }
    public static void setVisibility(int visibility) { _visibility = visibility; }
    public static void setDtList(List<Long> dts) { _dayTimes = dts; }
    public static void setTempsList(List<Double> temps) { _temps = temps; }
    public static void setRainPrepList(List<Double> rainPreps) { _rainPreps = rainPreps; }
    public static void setSnowPrepList(List<Double> snowPreps) { _snowPreps = snowPreps; }

    public void transactFragment(Fragment fragment, boolean reload) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (reload) {
            getSupportFragmentManager().popBackStack();
        }
        transaction.replace(R.id.view_pager, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, new HomeFragment()).commit();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new getJSONData().execute(currentForecast);
        new getForecastData().execute(fiveDaysForecast);
        //transactFragment(new HomeFragment(), false);

        //Button btn = (Button)findViewById(R.id.button);

       /* btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String fiveDaysForecast = "http://api.openweathermap.org/data/2.5/forecast?id=7531002&appid=50768df1f9a4be14d70a612605801e5c";
                String currentForecast = "http://api.openweathermap.org/data/2.5/weather?id=7531002&appid=50768df1f9a4be14d70a612605801e5c";
                new getJSONData().execute(fiveDaysForecast);
                new getJSONObject().execute(currentForecast);
            }
        });*/
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch(item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.forecast:
                            selectedFragment = new ForecastFragment();
                            break;
                        case R.id.more_info:
                            selectedFragment = new MoreInfoFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, selectedFragment).commit();

                    return true;
                }
            };

    private class getJSONData extends AsyncTask<String, Void, MainParameters>{
        private Reader json;
        private MainParameters response;

        @Override
        protected MainParameters doInBackground(String... urls){
            try{
                json = RestAPIService.getStream(urls[0]);
                Log.d("ddd", "getStream");
                Gson gson = new Gson();
                response = gson.fromJson(json, MainParameters.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(MainParameters response){
            try{
                List<Weather> weatherDescription = response.weather;
                String mainDescription = "", advDescription = "";
                for (Weather weather : weatherDescription){
                    mainDescription = weather.main;
                    advDescription = weather.description;
                }

                setCityName(response.cityName);
                setTemperature(response.main.temp);
                setFeelsLike(response.main.feels_like);
                setTimeFrom(response.daytime);
                setWindMainDescription(mainDescription);
                setWindAdvDescription(advDescription);
                setWindSpeed(response.wind.windSpeed);
                setWindDegree(response.wind.windDegree);
                setTempMax(response.main.temp_max);
                setTempMin(response.main.temp_min);
                setSunrise(response.sys.sunrise);
                setSunset(response.sys.sunset);
                setPressure(response.main.pressure);
                setHumidity(response.main.humidity);
                setVisibility(response.visibility);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private class getForecastData extends AsyncTask<String, Void, ForecastParams>{
//        private Reader json;
        private ForecastParams response;
//        InputStreamReader uuu;
        private String json = "{\"cod\":\"200\",\"message\":0,\"cnt\":40,\"list\":[{\"dt\":1585008000,\"main\":{\"temp\":270.33,\"feels_like\":265.45,\"temp_min\":270.33,\"temp_max\":272.6,\"pressure\":1041,\"sea_level\":1041,\"grnd_level\":1031,\"humidity\":55,\"temp_kf\":-2.27},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.55,\"deg\":187},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-24 00:00:00\"},{\"dt\":1585018800,\"main\":{\"temp\":270.67,\"feels_like\":265.91,\"temp_min\":270.67,\"temp_max\":272.37,\"pressure\":1040,\"sea_level\":1040,\"grnd_level\":1030,\"humidity\":47,\"temp_kf\":-1.7},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.22,\"deg\":205},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-24 03:00:00\"},{\"dt\":1585029600,\"main\":{\"temp\":272.1,\"feels_like\":267.48,\"temp_min\":272.1,\"temp_max\":273.24,\"pressure\":1041,\"sea_level\":1041,\"grnd_level\":1031,\"humidity\":42,\"temp_kf\":-1.14},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2,\"deg\":226},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-24 06:00:00\"},{\"dt\":1585040400,\"main\":{\"temp\":276.21,\"feels_like\":272.08,\"temp_min\":276.21,\"temp_max\":276.78,\"pressure\":1041,\"sea_level\":1041,\"grnd_level\":1031,\"humidity\":31,\"temp_kf\":-0.57},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":69},\"wind\":{\"speed\":1.3,\"deg\":212},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-24 09:00:00\"},{\"dt\":1585051200,\"main\":{\"temp\":278.58,\"feels_like\":274.17,\"temp_min\":278.58,\"temp_max\":278.58,\"pressure\":1040,\"sea_level\":1040,\"grnd_level\":1030,\"humidity\":30,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":44},\"wind\":{\"speed\":1.85,\"deg\":168},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-24 12:00:00\"},{\"dt\":1585062000,\"main\":{\"temp\":278.32,\"feels_like\":273.24,\"temp_min\":278.32,\"temp_max\":278.32,\"pressure\":1039,\"sea_level\":1039,\"grnd_level\":1028,\"humidity\":36,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.04,\"deg\":154},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-24 15:00:00\"},{\"dt\":1585072800,\"main\":{\"temp\":275.39,\"feels_like\":270.8,\"temp_min\":275.39,\"temp_max\":275.39,\"pressure\":1038,\"sea_level\":1038,\"grnd_level\":1028,\"humidity\":48,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.47,\"deg\":190},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-24 18:00:00\"},{\"dt\":1585083600,\"main\":{\"temp\":273.96,\"feels_like\":269.22,\"temp_min\":273.96,\"temp_max\":273.96,\"pressure\":1039,\"sea_level\":1039,\"grnd_level\":1029,\"humidity\":49,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.55,\"deg\":190},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-24 21:00:00\"},{\"dt\":1585094400,\"main\":{\"temp\":273.64,\"feels_like\":268.89,\"temp_min\":273.64,\"temp_max\":273.64,\"pressure\":1039,\"sea_level\":1039,\"grnd_level\":1029,\"humidity\":48,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.51,\"deg\":196},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-25 00:00:00\"},{\"dt\":1585105200,\"main\":{\"temp\":273.5,\"feels_like\":268.57,\"temp_min\":273.5,\"temp_max\":273.5,\"pressure\":1038,\"sea_level\":1038,\"grnd_level\":1028,\"humidity\":46,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.68,\"deg\":206},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-25 03:00:00\"},{\"dt\":1585116000,\"main\":{\"temp\":274.4,\"feels_like\":269.33,\"temp_min\":274.4,\"temp_max\":274.4,\"pressure\":1038,\"sea_level\":1038,\"grnd_level\":1028,\"humidity\":43,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.88,\"deg\":196},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-25 06:00:00\"},{\"dt\":1585126800,\"main\":{\"temp\":277.4,\"feels_like\":272.2,\"temp_min\":277.4,\"temp_max\":277.4,\"pressure\":1039,\"sea_level\":1039,\"grnd_level\":1029,\"humidity\":36,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":67},\"wind\":{\"speed\":3.12,\"deg\":167},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-25 09:00:00\"},{\"dt\":1585137600,\"main\":{\"temp\":278.76,\"feels_like\":273.69,\"temp_min\":278.76,\"temp_max\":278.76,\"pressure\":1038,\"sea_level\":1038,\"grnd_level\":1027,\"humidity\":37,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":83},\"wind\":{\"speed\":3.12,\"deg\":134},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-25 12:00:00\"},{\"dt\":1585148400,\"main\":{\"temp\":277.84,\"feels_like\":272.78,\"temp_min\":277.84,\"temp_max\":277.84,\"pressure\":1036,\"sea_level\":1036,\"grnd_level\":1026,\"humidity\":48,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":95},\"wind\":{\"speed\":3.45,\"deg\":97},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-25 15:00:00\"},{\"dt\":1585159200,\"main\":{\"temp\":276.1,\"feels_like\":271.83,\"temp_min\":276.1,\"temp_max\":276.1,\"pressure\":1035,\"sea_level\":1035,\"grnd_level\":1025,\"humidity\":64,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":88},\"wind\":{\"speed\":2.66,\"deg\":113},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-25 18:00:00\"},{\"dt\":1585170000,\"main\":{\"temp\":275.11,\"feels_like\":270.64,\"temp_min\":275.11,\"temp_max\":275.11,\"pressure\":1036,\"sea_level\":1036,\"grnd_level\":1026,\"humidity\":61,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":2.69,\"deg\":162},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-25 21:00:00\"},{\"dt\":1585180800,\"main\":{\"temp\":274.11,\"feels_like\":269.22,\"temp_min\":274.11,\"temp_max\":274.11,\"pressure\":1035,\"sea_level\":1035,\"grnd_level\":1025,\"humidity\":58,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.06,\"deg\":158},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-26 00:00:00\"},{\"dt\":1585191600,\"main\":{\"temp\":273.58,\"feels_like\":268.47,\"temp_min\":273.58,\"temp_max\":273.58,\"pressure\":1035,\"sea_level\":1035,\"grnd_level\":1024,\"humidity\":58,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.31,\"deg\":148},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-26 03:00:00\"},{\"dt\":1585202400,\"main\":{\"temp\":274.37,\"feels_like\":269,\"temp_min\":274.37,\"temp_max\":274.37,\"pressure\":1034,\"sea_level\":1034,\"grnd_level\":1024,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.65,\"deg\":136},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-26 06:00:00\"},{\"dt\":1585213200,\"main\":{\"temp\":277.9,\"feels_like\":271.97,\"temp_min\":277.9,\"temp_max\":277.9,\"pressure\":1034,\"sea_level\":1034,\"grnd_level\":1024,\"humidity\":46,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.62,\"deg\":120},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-26 09:00:00\"},{\"dt\":1585224000,\"main\":{\"temp\":279.92,\"feels_like\":273.79,\"temp_min\":279.92,\"temp_max\":279.92,\"pressure\":1033,\"sea_level\":1033,\"grnd_level\":1023,\"humidity\":47,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":5.23,\"deg\":100},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-26 12:00:00\"},{\"dt\":1585234800,\"main\":{\"temp\":280.6,\"feels_like\":274.93,\"temp_min\":280.6,\"temp_max\":280.6,\"pressure\":1031,\"sea_level\":1031,\"grnd_level\":1022,\"humidity\":50,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.82,\"deg\":99},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-26 15:00:00\"},{\"dt\":1585245600,\"main\":{\"temp\":279.2,\"feels_like\":273.42,\"temp_min\":279.2,\"temp_max\":279.2,\"pressure\":1031,\"sea_level\":1031,\"grnd_level\":1021,\"humidity\":57,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":5.06,\"deg\":110},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-26 18:00:00\"},{\"dt\":1585256400,\"main\":{\"temp\":277.5,\"feels_like\":271.87,\"temp_min\":277.5,\"temp_max\":277.5,\"pressure\":1031,\"sea_level\":1031,\"grnd_level\":1021,\"humidity\":60,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.68,\"deg\":127},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-26 21:00:00\"},{\"dt\":1585267200,\"main\":{\"temp\":276.09,\"feels_like\":270.98,\"temp_min\":276.09,\"temp_max\":276.09,\"pressure\":1030,\"sea_level\":1030,\"grnd_level\":1020,\"humidity\":66,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.93,\"deg\":126},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-27 00:00:00\"},{\"dt\":1585278000,\"main\":{\"temp\":275.83,\"feels_like\":270.72,\"temp_min\":275.83,\"temp_max\":275.83,\"pressure\":1029,\"sea_level\":1029,\"grnd_level\":1019,\"humidity\":69,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":61},\"wind\":{\"speed\":3.99,\"deg\":115},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-27 03:00:00\"},{\"dt\":1585288800,\"main\":{\"temp\":276.42,\"feels_like\":270.51,\"temp_min\":276.42,\"temp_max\":276.42,\"pressure\":1028,\"sea_level\":1028,\"grnd_level\":1018,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":80},\"wind\":{\"speed\":5.1,\"deg\":124},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-27 06:00:00\"},{\"dt\":1585299600,\"main\":{\"temp\":279.29,\"feels_like\":273.38,\"temp_min\":279.29,\"temp_max\":279.29,\"pressure\":1027,\"sea_level\":1027,\"grnd_level\":1017,\"humidity\":62,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":65},\"wind\":{\"speed\":5.48,\"deg\":118},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-27 09:00:00\"},{\"dt\":1585310400,\"main\":{\"temp\":282.95,\"feels_like\":276.73,\"temp_min\":282.95,\"temp_max\":282.95,\"pressure\":1026,\"sea_level\":1026,\"grnd_level\":1015,\"humidity\":55,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":58},\"wind\":{\"speed\":6.31,\"deg\":124},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-27 12:00:00\"},{\"dt\":1585321200,\"main\":{\"temp\":284,\"feels_like\":277.92,\"temp_min\":284,\"temp_max\":284,\"pressure\":1023,\"sea_level\":1023,\"grnd_level\":1013,\"humidity\":54,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":6.28,\"deg\":138},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-27 15:00:00\"},{\"dt\":1585332000,\"main\":{\"temp\":281.76,\"feels_like\":276.29,\"temp_min\":281.76,\"temp_max\":281.76,\"pressure\":1022,\"sea_level\":1022,\"grnd_level\":1013,\"humidity\":62,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":73},\"wind\":{\"speed\":5.36,\"deg\":138},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-27 18:00:00\"},{\"dt\":1585342800,\"main\":{\"temp\":279.56,\"feels_like\":274.63,\"temp_min\":279.56,\"temp_max\":279.56,\"pressure\":1022,\"sea_level\":1022,\"grnd_level\":1013,\"humidity\":61,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":32},\"wind\":{\"speed\":4.09,\"deg\":152},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-27 21:00:00\"},{\"dt\":1585353600,\"main\":{\"temp\":277.85,\"feels_like\":273.84,\"temp_min\":277.85,\"temp_max\":277.85,\"pressure\":1021,\"sea_level\":1021,\"grnd_level\":1011,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"clouds\":{\"all\":19},\"wind\":{\"speed\":2.75,\"deg\":155},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-28 00:00:00\"},{\"dt\":1585364400,\"main\":{\"temp\":277.18,\"feels_like\":272.9,\"temp_min\":277.18,\"temp_max\":277.18,\"pressure\":1019,\"sea_level\":1019,\"grnd_level\":1009,\"humidity\":70,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":3.08,\"deg\":160},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-28 03:00:00\"},{\"dt\":1585375200,\"main\":{\"temp\":277.84,\"feels_like\":273.52,\"temp_min\":277.84,\"temp_max\":277.84,\"pressure\":1018,\"sea_level\":1018,\"grnd_level\":1009,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":53},\"wind\":{\"speed\":3.19,\"deg\":166},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-28 06:00:00\"},{\"dt\":1585386000,\"main\":{\"temp\":282.31,\"feels_like\":277.94,\"temp_min\":282.31,\"temp_max\":282.31,\"pressure\":1018,\"sea_level\":1018,\"grnd_level\":1008,\"humidity\":56,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.59,\"deg\":156},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-28 09:00:00\"},{\"dt\":1585396800,\"main\":{\"temp\":284.9,\"feels_like\":280.76,\"temp_min\":284.9,\"temp_max\":284.9,\"pressure\":1016,\"sea_level\":1016,\"grnd_level\":1006,\"humidity\":52,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3.58,\"deg\":137},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-28 12:00:00\"},{\"dt\":1585407600,\"main\":{\"temp\":284.54,\"feels_like\":280.88,\"temp_min\":284.54,\"temp_max\":284.54,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":1005,\"humidity\":55,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":3,\"deg\":103},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2020-03-28 15:00:00\"},{\"dt\":1585418400,\"main\":{\"temp\":282.02,\"feels_like\":279.12,\"temp_min\":282.02,\"temp_max\":282.02,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":1005,\"humidity\":66,\"temp_kf\":0},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02n\"}],\"clouds\":{\"all\":23},\"wind\":{\"speed\":1.97,\"deg\":97},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-28 18:00:00\"},{\"dt\":1585429200,\"main\":{\"temp\":281.41,\"feels_like\":278.68,\"temp_min\":281.41,\"temp_max\":281.41,\"pressure\":1014,\"sea_level\":1014,\"grnd_level\":1004,\"humidity\":70,\"temp_kf\":0},\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":98},\"wind\":{\"speed\":1.79,\"deg\":149},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2020-03-28 21:00:00\"}],\"city\":{\"id\":7531002,\"name\":\"Gdańsk\",\"coord\":{\"lat\":54.3611,\"lon\":18.6898},\"country\":\"PL\",\"timezone\":3600,\"sunrise\":1584938361,\"sunset\":1584983040}}";
        @Override
        protected ForecastParams doInBackground(String... urls){
            try{
//                json = RestAPIService.getStream(urls[0]);
//                uuu = RestAPIService.getStream2(urls[0]);
//                Log.d("ddd", "getStream2");
//                RestAPIService.readJsonStream(uuu);
                Gson gson = new GsonBuilder().serializeNulls().create();
                Log.d("ddd", "response");
                response = gson.fromJson(json, ForecastParams.class);

            }catch (Exception e){
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(ForecastParams response){
            try{
//                json2.beginObject();
//                Gson gson = new GsonBuilder().create();
//
//                while(json2.hasNext()){
//                    String name = json2.nextName();
//                    if(name.equals("list")){
//                        ForecastParams response = gson.fromJson(json2, ForecastParams.class);
//
//                    }
//                }
                //                json2 = FilesHandler.readerToString(json);
//                Log.d("json2", json2);

                Log.d("ddd", "gson");

                List<_List> forecastList = response.list;
                List<Long> dayTime = new ArrayList<>();
                List<Double> temps = new ArrayList<>();
                List<Double> rainPrep = new ArrayList<>();
                List<Double> snowPrep = new ArrayList<>();

                for (_List weather : forecastList){
                    dayTime.add(weather.dt);
                    temps.add(weather.main.temp);
//                    Double rain = (weather.rain.rainPrep == null) ? 0.0 : weather.rain.rainPrep;
//                    Double snow = (weather.snow.snowPrep == null) ? 0.0 : weather.snow.snowPrep;
//                    rainPrep.add(rain);
//                    snowPrep.add(snow);
                }

                setDtList(dayTime);
                setTempsList(temps);
//                setRainPrepList(rainPrep);
//                setSnowPrepList(snowPrep);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
//
//    private class getJSONStream extends AsyncTask<String, Void, InputStream>{
//        private InputStream stream;
//
//        @Override
//        protected InputStream doInBackground(String... urls){
//            try{
//                stream = RestAPIService.getStream(urls[0]);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            return stream;
//        }
//
//        @Override
//        protected void onPostExecute(InputStream jsonObject){
//            //TextView textView4 = (TextView)findViewById(R.id.textView4);
//
//            try{
//                String data = RestAPIService.getDataFromJSON(jsonObject, "time");
//                //textView4.setText(data);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
}

