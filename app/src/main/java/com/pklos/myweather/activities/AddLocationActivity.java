package com.pklos.myweather.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pklos.myweather.R;
import com.pklos.myweather.locations_database.LocationsDB;
import com.pklos.myweather.locations_database.MyWeatherExecutors;
import com.pklos.myweather.locations_model.Location;
import com.pklos.myweather.searchcity_model.SearchCityParams;
import com.pklos.myweather.searchcity_model._List2;
import com.pklos.myweather.utils.LocalDBUtils;
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

    private LinearLayout createCityRecordFromJSON(final int id, final String cityName, double lat, double lon){
//        TextView cityIDTextView = new TextView(this);
//        cityIDTextView.setTextSize(20);
//        cityIDTextView.setGravity(Gravity.CENTER_HORIZONTAL);
//        cityIDTextView.setText(String.valueOf(id));
        ImageButton addButton = new ImageButton(this);
        addButton.setImageResource(R.drawable.ic_plus_one_black_24dp);
        //addButton.setImageDrawable(getBaseContext().getResources().getDrawable(R.drawable.add_button_states));
        addButton.setMaxWidth(50);
        addButton.setMaxHeight(50);
        //addButton.setSelected(!addButton.isSelected());
        addButton.setOnClickListener(new ImageButton.OnClickListener(){

            @Override
            public void onClick(View v) {
                LocalDBUtils.insertDataToDB(getApplicationContext(), id, cityName);
                Toast.makeText(getApplicationContext(), R.string.added_city_alert, Toast.LENGTH_LONG).show();
                //LocalDBUtils.fetchLocationsData(getApplicationContext());
            }
        });

        TextView cityNameTextView = new TextView(this);
        cityNameTextView.setTextSize(20);
        cityNameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        cityNameTextView.setText(cityName);

        TextView latLonTextView = new TextView(this);
        latLonTextView.setTextSize(20);
        latLonTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        latLonTextView.setText(getString(R.string.lon_lat, lat, lon));

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setGravity(Gravity.START);
        ll.addView(cityNameTextView);
        ll.addView(latLonTextView);
        ll.addView(addButton);
        ll.setId(id);

        return ll;
    }

    public void searchCityButton(View view){
        TextView cityName = (TextView) findViewById(R.id.cityName);
        String param = cityName.getText().toString();
        if (!param.isEmpty()){
            StringBuilder JSONUrl = new StringBuilder("http://api.openweathermap.org/data/2.5/find?callback=&q=")
                    .append(param)
                    .append("&type=like&sort=population&cnt=30&appid=50768df1f9a4be14d70a612605801e5c");
//            ConstraintLayout mainLayout = (ConstraintLayout) findViewById((R.id.addLocationLayout));
//            int childViewsCounter = mainLayout.getChildCount();
//            if (childViewsCounter > 2){
//                for(int i = 3; i < childViewsCounter; i++){
//                    View viewToRemove = mainLayout.getChildAt(i);
//                    mainLayout.removeView(viewToRemove);
//                }
//            }

            new getSearchJSONData().execute(JSONUrl.toString());
        }
        else Toast.makeText(this, R.string.type_city_alert, Toast.LENGTH_LONG).show();

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

        @SuppressLint("ResourceType")
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

//                set_count(count);
//                set_ids(ids);
//                set_cityNames(cityNames);
//                set_lat(lat);
//                set_lon(lon);

                ConstraintLayout mainLayout = (ConstraintLayout) findViewById((R.id.addLocationLayout));
                ConstraintSet constraintSet = new ConstraintSet();
                List<Integer> layoutsIds = new ArrayList<>();
                constraintSet.clone(mainLayout);

                LinearLayout countInfo = printResultsCountFromJSON(count);
                countInfo.setId(100);
                mainLayout.addView(countInfo);
                int countInfoID = countInfo.getId();
                constraintSet.connect(countInfoID, ConstraintSet.TOP, R.id.searchButton , ConstraintSet.BOTTOM, 0 );
                constraintSet.connect(countInfoID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0 );
                constraintSet.connect(countInfoID, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0 );
                constraintSet.constrainHeight(countInfoID, 200);
                constraintSet.applyTo(mainLayout);

                LinearLayout cityResult = createCityRecordFromJSON(ids.get(0), cityNames.get(0), lat.get(0), lon.get(0));
                mainLayout.addView(cityResult);
                int cityResultID = cityResult.getId();
                constraintSet.connect(cityResultID, ConstraintSet.TOP, countInfoID, ConstraintSet.BOTTOM, 0 );
                constraintSet.connect(cityResultID, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0 );
                constraintSet.connect(cityResultID, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0 );
                constraintSet.constrainHeight(cityResultID, 350);
                constraintSet.applyTo(mainLayout);

                layoutsIds.add(cityResultID);

                for (int index = 1; index < count; index++){
                    LinearLayout cityResultNext = createCityRecordFromJSON(ids.get(index), cityNames.get(index), lat.get(index), lon.get(index));
                    mainLayout.addView(cityResultNext);
                    int cityResultNextID = cityResultNext.getId();
                    layoutsIds.add(cityResultNextID);
                    constraintSet.connect(layoutsIds.get(index), ConstraintSet.TOP, layoutsIds.get(index - 1), ConstraintSet.BOTTOM, 0 );
                    constraintSet.connect(layoutsIds.get(index), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0 );
                    constraintSet.connect(layoutsIds.get(index), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0 );
                    constraintSet.constrainHeight(layoutsIds.get(index), 350);
                    constraintSet.applyTo(mainLayout);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}