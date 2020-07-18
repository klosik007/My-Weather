package com.pklos.myweather.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import com.pklos.myweather.R;
import com.pklos.myweather.activities.MainActivity;
import com.pklos.myweather.locations_database.LocationsDB;
import com.pklos.myweather.locations_database.MyWeatherExecutors;
import com.pklos.myweather.locations_model.Location;
import com.pklos.myweather.utils.LocalDBUtils;

import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        final PreferenceCategory citiesCategory = (PreferenceCategory) findPreference("cities_to_delete");

        final LocationsDB appDB = LocationsDB.getInstance(getContext());
        MyWeatherExecutors.getInstance().getDiskIO().execute(new Runnable(){
            @Override
            public void run() {
                final List<Location> locations = appDB.locationsDao().getLocationsList();
                for (final Location location : locations){
                    Preference cityToDelete = new Preference(getContext());
                    cityToDelete.setTitle(location.getCityName());
                    cityToDelete.setSummary(R.string.touch_city_tip);
                    cityToDelete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            LocalDBUtils.deleteDataFromDB(getContext(), location);
                            Toast.makeText(getContext(), R.string.on_city_remove, Toast.LENGTH_SHORT).show();
                            refreshMainActivity();
                            return true;
                        }
                    });
                    citiesCategory.addPreference(cityToDelete);
                }
            }
        });

        final PreferenceCategory languageCategory = (PreferenceCategory) findPreference("language");
        Preference languageCategoryPref1 = languageCategory.getPreference(0);
        languageCategoryPref1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                refreshMainActivity();
                return true;
            }
        });
    }

    private void refreshMainActivity(){
        Intent refresh = new Intent(getContext(), MainActivity.class);
        startActivity(refresh);
    }
}
