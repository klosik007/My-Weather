package com.pklos.myweather.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pklos.myweather.fragments.ForecastFragment;
import com.pklos.myweather.locations_database.LocationsDB;
import com.pklos.myweather.locations_database.MyWeatherExecutors;
import com.pklos.myweather.locations_model.Location;
import com.pklos.myweather.weatherforecast_openweather_model.ForecastParams;
import com.pklos.myweather.fragments.HomeFragment;
import com.pklos.myweather.weatherforecast_openweather_model.MainParameters;
import com.pklos.myweather.fragments.MoreInfoFragment;
import com.pklos.myweather.R;
import com.pklos.myweather.utils.RestAPIService;
import com.pklos.myweather.weatherforecast_openweather_model.Weather;
import com.pklos.myweather.weatherforecast_openweather_model._List;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //OpenWeather
    private static double _feelsLike = 0;
    private static String _city = "";
    private static double _temp = 0;
    private static long _timeFrom = 0L;
    private static String _weatherIcon = "";
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
    private static ArrayList<Long> _dayTimes = new ArrayList<>();
    private static ArrayList<Double> _temps = new ArrayList<>();
    private static ArrayList<Double> _rainPreps = new ArrayList<>();
    private static ArrayList<Double> _snowPreps = new ArrayList<>();

    private String cityID;
    private StringBuilder fiveDaysForecast;
    private StringBuilder currentForecast;

    //private Context context;
    private SharedPreferences sharedPreferences;
    //private String data_source;
    private String language;

    private Locale myLocale;

    //private String forecastYrno;

    public static String getCityName() {
        return _city;
    }

    public static double getTemp() {
        return _temp;
    }

    public static double getFeelsLike() {
        return _feelsLike;
    }

    public static long getTimeFrom() {
        return _timeFrom;
    }

    public static String getWeatherIcon() { return _weatherIcon; }

    public static String getWindAdvDescription() {
        return _advWindDescription;
    }

    public static double getWindSpeed() {
        return _windSpeed;
    }

    public static int getWindDegree() {
        return _windDegree;
    }

    public static double getTempMax() {
        return _tempMax;
    }

    public static double getTempMin() {
        return _tempMin;
    }

    public static long getSunrise() {
        return _sunrise;
    }

    public static long getSunset() {
        return _sunset;
    }

    public static int getPressure() {
        return _pressure;
    }

    public static int getHumidity() {
        return _humidity;
    }

    public static int getVisibility() {
        return _visibility;
    }

    public static ArrayList<Long> getDtList() {
        return _dayTimes;
    }

    public static ArrayList<Double> getTempsList() {
        return _temps;
    }

    public static ArrayList<Double> getRainPrepsList() {
        return _rainPreps;
    }

    public static ArrayList<Double> getSnowPrepsList() {
        return _snowPreps;
    }

    //---------------------------------------------------------------------------

    public static void setCityName(String city) {
        _city = city;
    }

    public static void setTemperature(double temp) {
        _temp = temp;
    }

    public static void setFeelsLike(double feelsLike) {
        _feelsLike = feelsLike;
    }

    public static void setTimeFrom(long timeFrom) {
        _timeFrom = timeFrom;
    }

    public static void setWeatherIconURLStr(String weatherIcon) {
        _weatherIcon = weatherIcon;
    }

    public static void setWeatherAdvDescription(String advDescription) {
        _advWindDescription = advDescription;
    }

    public static void setWindSpeed(double windSpeed) {
        _windSpeed = windSpeed;
    }

    public static void setWindDegree(int windDegree) {
        _windDegree = windDegree;
    }

    public static void setTempMax(double tempMax) {
        _tempMax = tempMax;
    }

    public static void setTempMin(double tempMin) {
        _tempMin = tempMin;
    }

    public static void setSunrise(long sunrise) {
        _sunrise = sunrise;
    }

    public static void setSunset(long sunset) {
        _sunset = sunset;
    }

    public static void setPressure(int pressure) {
        _pressure = pressure;
    }

    public static void setHumidity(int humidity) {
        _humidity = humidity;
    }

    public static void setVisibility(int visibility) {
        _visibility = visibility;
    }

    public static void setDtList(ArrayList<Long> dts) {
        _dayTimes = dts;
    }

    public static void setTempsList(ArrayList<Double> temps) {
        _temps = temps;
    }

    public static void setRainPrepList(ArrayList<Double> rainPreps) {
        _rainPreps = rainPreps;
    }

    public static void setSnowPrepList(ArrayList<Double> snowPreps) {
        _snowPreps = snowPreps;
    }


    //yr.no
//    private static ArrayList<String> _timeStrings = new ArrayList<>();
//    public static ArrayList<String> get_timeStrings() {
//        return _timeStrings;
//    }
//    public static void set_timeStrings(ArrayList<String> _timeStrings) {
//        MainActivity._timeStrings = _timeStrings;
//    }

    //navigation
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

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

//    private void setLanguage(String lang){
//        myLocale = new Locale(lang);
//        Resources res = getResources();
//        DisplayMetrics dm = res.getDisplayMetrics();
//        Configuration conf = res.getConfiguration();
//        conf.locale = myLocale;
//        res.updateConfiguration(conf, dm);
//        Intent refresh = new Intent(this, MainActivity.class);
//        startActivity(refresh);
//    }

//    private void insertInitialData(){
//        final LocationsDB appDB = LocationsDB.getInstance(this);
//        final Location location = new Location("Gdańsk", "7000000", true);
//        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
//            @Override
//            public void run() {
//                appDB.locationsDao().insertLocation(location);
//            }
//        });
//    }
//
//    private void fetchLocationsData(){
//        final LocationsDB appDB = LocationsDB.getInstance(this);
//        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
//            @Override
//            public void run() {
//                final List<Location> locations = appDB.locationsDao().getLocationsList();
//                for (Location location : locations){
//                    Log.d("Locations", location.getId() + location.getCityName() + location.getCityID() + location.isDefault());
//                }
//            }
//        });
//    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //OpenWeather
        //default cityID
        cityID = "3099434";//Gdańsk
        fiveDaysForecast = new StringBuilder("http://api.openweathermap.org/data/2.5/forecast?id=")
                .append(cityID)
                .append("&appid=50768df1f9a4be14d70a612605801e5c");
        currentForecast = new StringBuilder("http://api.openweathermap.org/data/2.5/weather?id=")
                .append(cityID)
                .append("&appid=50768df1f9a4be14d70a612605801e5c");

        new getJSONData().execute(currentForecast.toString());
        new getForecastData().execute(fiveDaysForecast.toString());

        //Yr.no default city - Gdańsk
        //forecastYrno = "http://api.met.no/weatherapi/locationforecast/2.0/complete?lat=54.3&lon=18.5";

        //settings
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //data_source = sharedPreferences.getString(getString(R.string.sp_key_data_source), "OPEN_WEATHER");
        language = sharedPreferences.getString(getString(R.string.sp_key_language), "ENGLISH");

        switch(language){
            case "ENGLISH":
                //setLocale('en');
                break;

            case "POLISH":
                //setLocale('pl');
                break;
        }

//        switch(data_source){
//            case "OPEN_WEATHER"://if in preferences OpenWeather
//                new getJSONData().execute(currentForecast.toString());
//                new getForecastData().execute(fiveDaysForecast.toString());
//                break;
//            case "YR_NO"://if in preferences Yr.no
//                new getJSONData().execute(currentForecast.toString());
//                new getYrnoForecastData().execute(forecastYrno);
//                break;
//        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        final SubMenu subMenu = menu.addSubMenu(0, 0, 0, "Locations");
        final LocationsDB appDB = LocationsDB.getInstance(this);


        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                final List<Location> locations = appDB.locationsDao().getLocationsList();
                for (final Location location : locations){
                    subMenu.add(0, location.getId(), 0, location.getCityName())
                           .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            cityID = location.getCityID();
                            fiveDaysForecast = new StringBuilder("http://api.openweathermap.org/data/2.5/forecast?id=")
                                    .append(cityID)
                                    .append("&appid=50768df1f9a4be14d70a612605801e5c");
                            currentForecast = new StringBuilder("http://api.openweathermap.org/data/2.5/weather?id=")
                                    .append(cityID)
                                    .append("&appid=50768df1f9a4be14d70a612605801e5c");
                            new getJSONData().execute(currentForecast.toString());
                            new getForecastData().execute(fiveDaysForecast.toString());
                            return true;
                        }
                    });
                }
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.view_pager, new HomeFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.add_location:
                Intent addLocationIntent = new Intent(this, AddLocationActivity.class);
                startActivity(addLocationIntent);
                return true;
        }

        return true;
    }

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
                String icon = "", advDescription = "";
                for (Weather weather : weatherDescription){
                    advDescription = weather.description;
                    icon = weather.icon;
                }

                //if OpenWeather
                setCityName(response.cityName);
                setTemperature(response.main.temp);
                setFeelsLike(response.main.feels_like);
                setTimeFrom(response.daytime);
                setWeatherAdvDescription(advDescription);
                setWeatherIconURLStr(icon);
                setWindSpeed(response.wind.windSpeed);
                setWindDegree(response.wind.windDegree);
                setTempMax(response.main.temp_max);
                setTempMin(response.main.temp_min);
                setSunrise(response.sys.sunrise);
                setSunset(response.sys.sunset);
                setPressure(response.main.pressure);
                setHumidity(response.main.humidity);
                setVisibility(response.visibility);

                //if Yr.no
//                setCityName(response.cityName);
//                setSunrise(response.sys.sunrise);
//                setSunset(response.sys.sunset);
//                setVisibility(response.visibility);

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
                ArrayList<Long> dayTime = new ArrayList<>();
                ArrayList<Double> temps = new ArrayList<>();
                ArrayList<Double> rainPrep = new ArrayList<>();
                ArrayList<Double> snowPrep = new ArrayList<>();

                for (_List weather : forecastList){
                    dayTime.add(weather.dt);
                    temps.add(weather.main.temp);
                    Double rain = weather.rain != null ? weather.rain.rainPrep : 0.0;
                    Double snow = weather.snow != null ? weather.snow.snowPrep : 0.0;
                    rainPrep.add(rain);
                    snowPrep.add(snow);
                }

                setDtList(dayTime);
                setTempsList(temps);
                setRainPrepList(rainPrep);
                setSnowPrepList(snowPrep);

                transactFragment(new HomeFragment(), true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

//    private class getYrnoForecastData extends AsyncTask<String, Void, ForecastYr>{
//        private String json;
//        private ForecastYr response;
//
//        @Override
//        protected ForecastYr doInBackground(String... urls){
//            try{
//                json = RestAPIService.getStream(urls[0]);
//                Log.d("ddd", "getStreamYrNo");
//                Gson gson = new Gson();
//                response = gson.fromJson(json, ForecastYr.class);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(ForecastYr response){
//            try{
//                List<Timeseries> forecastList = response.timeseries;
//                ArrayList<String> dayTime = new ArrayList<>();
//                ArrayList<Double> temps = new ArrayList<>();
//                ArrayList<Double> rainPrep = new ArrayList<>();
//                ArrayList<Double> pressureList = new ArrayList<>();
//                ArrayList<Double> humidities = new ArrayList<>();
//                ArrayList<Double> windSpeeds = new ArrayList<>();
//                ArrayList<Double> windDirections = new ArrayList<>();
//                ArrayList<Double> max_temps = new ArrayList<>();
//                ArrayList<Double> min_temps = new ArrayList<>();
//
//                for (Timeseries weather : forecastList){
//                    dayTime.add(weather.time);
//                    temps.add(weather.data.instant.details.air_temperature);
//                    rainPrep.add(weather.data.next_6_hours.details.precipitation_amount);
//                    pressureList.add(weather.data.instant.details.air_pressure);
//                    humidities.add(weather.data.instant.details.relative_humidity);
//                    windSpeeds.add(weather.data.instant.details.wind_speed);
//                    windDirections.add(weather.data.instant.details.wind_from_direction);
//                    max_temps.add(weather.data.next_6_hours.details.air_temperature_max);
//                    min_temps.add(weather.data.next_6_hours.details.air_temperature_min);
//                }
//
//                //forecast
//                set_timeStrings(dayTime);
//                setTempsList(temps);
//                setRainPrepList(rainPrep);
//
//                //more info
//                setTemperature(temps.get(0));
//                setFeelsLike(temps.get(0));//no feels like data in yr.no json
//                setPressure(pressureList.get(0).intValue());
//                setHumidity(humidities.get(0).intValue());
//                setWindSpeed(windSpeeds.get(0).intValue());
//                setWindDegree(windDirections.get(0).intValue());
//                setTempMax(max_temps.get(0).intValue());
//                setTempMin(min_temps.get(0).intValue());
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
}