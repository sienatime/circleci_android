package com.dotheastro.android.circleciunofficial.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.CircleApp;
import com.dotheastro.android.circleciunofficial.models.bus.ApiTokenChangedEvent;
import com.squareup.otto.Bus;

/**
 * Created by Siena on 10/11/2014.
 */
public class PreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        addPreferencesFromResource(R.xml.prefs);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String accessToken = sharedPreferences.getString("circleAPIToken", null);
        Bus bus = CircleApp.getInstance().getBus();

        bus.post(new ApiTokenChangedEvent(accessToken));
    }
}
