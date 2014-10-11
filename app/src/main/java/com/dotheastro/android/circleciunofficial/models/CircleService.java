package com.dotheastro.android.circleciunofficial.models;

import android.util.Log;

import com.dotheastro.android.circleciunofficial.interfaces.CircleAPI;
import com.dotheastro.android.circleciunofficial.models.bus.ApiErrorEvent;
import com.dotheastro.android.circleciunofficial.models.bus.BuildsLoadedEvent;
import com.dotheastro.android.circleciunofficial.models.bus.LoadBuildsEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Siena on 10/11/2014.
 */
public class CircleService {

    private Bus bus;
    private RestAdapter restAdapter;
    private CircleAPI api;

    public CircleService(Bus bus, String token) {
        this.bus = bus;
        this.restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://circleci.com")
                .setRequestInterceptor(new CircleRequestInterceptor(token))
                .build();
        this.api = restAdapter.create(CircleAPI.class);
    }

    @Subscribe
    public void getBuilds(LoadBuildsEvent event) {
        api.listBuilds(new Callback<Object>() {
            @Override
            public void success(Object builds, Response response) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")
                        .create();
                Build[] buildArray = gson.fromJson(gson.toJson(builds)
                        , Build[].class);
                bus.post(new BuildsLoadedEvent(buildArray));
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Retrofit error", error.toString());
                bus.post(new ApiErrorEvent(error));
            }
        });
    }
}
