package com.pklos.myweather.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pklos.myweather.R;
import com.pklos.myweather.searchcity_model.SearchCityParams;
import com.pklos.myweather.searchcity_model._List2;
import com.pklos.myweather.utils.RestAPIService;

import java.util.ArrayList;
import java.util.List;

public class AddLocationActivity extends AppCompatActivity {
    private static int _count;
    private static List<Integer> _ids = new ArrayList<>();
    private static List<String> _cityNames = new ArrayList<>();
    private static List<Double> _lat = new ArrayList<>();
    private static List<Double> _lon = new ArrayList<>();
//----------------------------------------------
//    public static int get_count() {
//        return _count;
//    }
//
//    public static List<Integer> get_ids() {
//        return _ids;
//    }
//
//    public static List<String> get_cityNames() {
//        return _cityNames;
//    }
//
//    public static List<Double> get_lat() {
//        return _lat;
//    }
//
//    public static List<Double> get_lon() {
//        return _lon;
//    }
//----------------------------------------------
    public static void set_count(int _count) {
        AddLocationActivity._count = _count;
    }
    public static void set_ids(List<Integer> _ids) {
        AddLocationActivity._ids = _ids;
    }
    public static void set_cityNames(List<String> _cityNames) {
        AddLocationActivity._cityNames = _cityNames;
    }
    public static void set_lat(List<Double> _lat) {
        AddLocationActivity._lat = _lat;
    }
    public static void set_lon(List<Double> _lon) {
        AddLocationActivity._lon = _lon;
    }
//----------------------------------------------

    private LinearLayout printResultsCountFromJSON(int count){
        TextView countTextView = new TextView(this);
        countTextView.setTextSize(20);
        countTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        countTextView.setText(getString(R.string.results_count, count));

        LinearLayout llCount = new LinearLayout(this);
        llCount.setOrientation(LinearLayout.VERTICAL);
        llCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llCount.setGravity(Gravity.START);
        llCount.addView(countTextView);

        return llCount;
    }

    private LinearLayout createCityRecordFromJSON(int id, String cityName, double lat, double lon){
        TextView cityIDTextView = new TextView(this);
        cityIDTextView.setTextSize(20);
        cityIDTextView.setGravity(Gravity.START);
        cityIDTextView.setText(id);

        TextView cityNameTextView = new TextView(this);
        cityNameTextView.setTextSize(20);
        cityNameTextView.setGravity(Gravity.START);
        cityNameTextView.setText(id);

        TextView latLonTextView = new TextView(this);
        latLonTextView.setTextSize(20);
        latLonTextView.setGravity(Gravity.START);
        latLonTextView.setText(getString(R.string.lon_lat, lon, lat));

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.LEFT);
        ll.addView(cityIDTextView);
        ll.addView(cityNameTextView);
        ll.addView(latLonTextView);
        ll.setId(id);

        return ll;
    }

    public void searchCityButton(View view){
        TextView cityName = (TextView) findViewById(R.id.cityName);
        String param = cityName.getText().toString();
        StringBuilder JSONUrl = new StringBuilder("http://openweathermap.org/data/2.5/find?callback=?&q=")
                                    .append(param)
                                    .append("&type=like&sort=population&cnt=30&appid=439d4b804bc8187953eb36d2a8c26a02");
        new getSearchJSONData().execute(JSONUrl.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
    }

    private class getSearchJSONData extends AsyncTask<String, Void, SearchCityParams> {
        private String json;
        private SearchCityParams response;

        @Override
        protected SearchCityParams doInBackground(String... urls){
            try{
                json = RestAPIService.getStream(urls[0]);
                Gson gson = new Gson();
                response = gson.fromJson(json, SearchCityParams.class);
            }catch (Exception e){
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(SearchCityParams response){
            try{
                int count = response.count;
                List<_List2> searchResults = response.list;

                List<Integer> ids = new ArrayList<>();
                List<String> cityNames = new ArrayList<>();
                List<Double> lat = new ArrayList<>();
                List<Double> lon = new ArrayList<>();

                for (_List2 results : searchResults){
                    ids.add(results.id);
                    cityNames.add(results.name);
                    lat.add(results.coord.lat);
                    lon.add(results.coord.lon);
                }

                set_count(count);
                set_ids(ids);
                set_cityNames(cityNames);
                set_lat(lat);
                set_lon(lon);

                ConstraintLayout mainLayout = (ConstraintLayout) findViewById((R.id.addLocationLayout));
                ConstraintSet constraintSet = new ConstraintSet();
                List<Integer> layoutsIds = new ArrayList<>();
                constraintSet.clone(mainLayout);

                LinearLayout countInfo = printResultsCountFromJSON(_count);
                mainLayout.addView(countInfo);
                int countInfoID = countInfo.getId();
                constraintSet.connect(countInfoID, ConstraintSet.TOP, R.id.searchButton, ConstraintSet.BOTTOM, 0 );

                LinearLayout cityResult = createCityRecordFromJSON(_ids.get(0), _cityNames.get(0), _lat.get(0), _lon.get(0));
                mainLayout.addView(cityResult);
                int cityResultID = cityResult.getId();
                constraintSet.connect(cityResultID, ConstraintSet.TOP, countInfoID, ConstraintSet.BOTTOM, 0 );
                layoutsIds.add(cityResultID);

                for (int index = 1; index < _count; index++){
                    LinearLayout cityResultNext = createCityRecordFromJSON(_ids.get(index), _cityNames.get(index), _lat.get(index), _lon.get(index));
                    mainLayout.addView(cityResultNext);
                    int cityResultNextID = cityResultNext.getId();
                    layoutsIds.add(cityResultNextID);
                    constraintSet.connect(layoutsIds.get(index), ConstraintSet.TOP, layoutsIds.get(index - 1), ConstraintSet.BOTTOM, 0 );
                }

                constraintSet.applyTo(mainLayout);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}