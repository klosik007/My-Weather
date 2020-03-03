package com.pklos.myweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static Map<String, Double> _mainParameters;
    private static String _city = "";
    //private String fiveDaysForecast = "http://api.openweathermap.org/data/2.5/forecast?id=7531002&appid=50768df1f9a4be14d70a612605801e5c";
    private String currentForecast = "http://api.openweathermap.org/data/2.5/weather?id=7531002&appid=50768df1f9a4be14d70a612605801e5c";

    public static String getCityName(){ return _city; }
    public static Map<String, Double> getMainParameters(){ return _mainParameters; }

    public static void setCityName(String city) { _city = city; }
    public static void setMainParameters(Map<String, Double> mainParameters) { _mainParameters = mainParameters; }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, new HomeFragment()).commit();
        new getJSONData().execute(currentForecast);

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

    private class getJSONData extends AsyncTask<String, Void, JsonReader>{
        private JsonReader json;
        //ProgressDialog progress;

        @Override
        protected JsonReader doInBackground(String... urls){
            try{
                json = RestAPIService.getStream(urls[0]);
                Log.d("ddd", "getStream");
            }catch (Exception e){
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(JsonReader json){
            try{
                setCityName(RestAPIService.readCity(json));
                setMainParameters(RestAPIService.readMainParameters(json));
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

//    private class getJSONObject extends AsyncTask<String, Void, JSONObject>{
//        private JSONObject obj;
//
//        @Override
//        protected JSONObject doInBackground(String... urls){
//            try{
//                obj = RestAPIService.fetchJSON(urls[0]);
//            }catch (InterruptedException e) {
//                e.printStackTrace();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//            return obj;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject){
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