package com.dotheastro.android.circleciunofficial.models;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.bus.ApiErrorEvent;
import com.dotheastro.android.circleciunofficial.models.bus.ApiTokenChangedEvent;
import com.dotheastro.android.circleciunofficial.models.bus.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * Created by Siena on 10/11/2014.
 */
public class CircleApp extends Application {
    private static CircleApp instance;
    private Bus bus = BusProvider.getInstance();
    public static CircleService service;

    public static CircleApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String token = prefs.getString("circleAPIToken", null);
        service = new CircleService(bus, token);
        bus.register(service);
        bus.register(this);
        super.onCreate();
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        String message = event.getMessage();
        if (event.getError().getMessage().equals("401 Unauthorized")) {
            message = getString(R.string.unauthorized);
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.e("CircleCIUnofficial", event.getMessage());
        Thread.dumpStack();
    }

    @Subscribe
    public void onApiTokenChanged(ApiTokenChangedEvent event) {
        bus.unregister(service);
        service = new CircleService(bus, event.getToken());
        bus.register(service);
    }

    public Bus getBus() {
        return bus;
    }
}
