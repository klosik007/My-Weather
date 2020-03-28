package com.pklos.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

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
        private String json;
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
        private ForecastParams response;
        private String json;
        @Override
        protected ForecastParams doInBackground(String... urls){
            try{
                json = RestAPIService.getStream(urls[0]);
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
}

