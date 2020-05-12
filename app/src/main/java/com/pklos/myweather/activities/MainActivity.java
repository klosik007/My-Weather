package com.pklos.myweather.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pklos.myweather.file_handlers.FilesHandler;
import com.pklos.myweather.fragments.ForecastFragment;
import com.pklos.myweather.weatherforecast_model.ForecastParams;
import com.pklos.myweather.fragments.HomeFragment;
import com.pklos.myweather.weatherforecast_model.MainParameters;
import com.pklos.myweather.fragments.MoreInfoFragment;
import com.pklos.myweather.R;
import com.pklos.myweather.utils.RestAPIService;
import com.pklos.myweather.weatherforecast_model.Weather;
import com.pklos.myweather.weatherforecast_model._List;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    private static ArrayList<Long> _dayTimes = new ArrayList<>();
    private static ArrayList<Double> _temps = new ArrayList<>();
    private static ArrayList<Double> _rainPreps = new ArrayList<>();
    private static ArrayList<Double> _snowPreps = new ArrayList<>();

    private String cityID;
    private StringBuilder fiveDaysForecast;
    private StringBuilder currentForecast;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

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

    public static String getWindMainDescription() {
        return _mainWindDescription;
    }

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

    public static void setWindMainDescription(String mainDescription) {
        _mainWindDescription = mainDescription;
    }

    public static void setWindAdvDescription(String advDescription) {
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

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        FilesHandler fileHandler = new FilesHandler(MainActivity.this);
        fileHandler.createCitiesFileOnStart();

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
        SubMenu subMenu = menu.addSubMenu(0, 0, 0, "Locations");
        subMenu.add(0, 0, 0, "Gdańsk").setOnMenuItemClickListener(menuItemClickListener);
        subMenu.add(0, 1, 0, "Damnica").setOnMenuItemClickListener(menuItemClickListener);



        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.view_pager, new HomeFragment())
                .commit();
        //transactFragment(new HomeFragment(), true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.settings_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.settings:
//                Intent settingsIntent = new Intent(this, SettingsActivity.class);
//                startActivity(settingsIntent);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

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

    private MenuItem.OnMenuItemClickListener menuItemClickListener =
            new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch(item.getItemId()){
                        case 0:
                            Toast.makeText(MainActivity.this, "Gdańsk", Toast.LENGTH_SHORT).show();
                            return true;
                        case 1:
                            cityID = "3100671";//Damnica
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
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

