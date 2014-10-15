package com.dotheastro.android.circleciunofficial.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.interfaces.CircleAPI;
import com.dotheastro.android.circleciunofficial.models.bus.ApiErrorEvent;
import com.dotheastro.android.circleciunofficial.models.bus.BuildsLoadedEvent;
import com.dotheastro.android.circleciunofficial.models.bus.CancelBuildEvent;
import com.dotheastro.android.circleciunofficial.models.bus.CancelBuildSuccessful;
import com.dotheastro.android.circleciunofficial.models.bus.LoadBuildsEvent;
import com.dotheastro.android.circleciunofficial.models.bus.RetryBuildEvent;
import com.dotheastro.android.circleciunofficial.models.bus.RetrySuccessfulEvent;
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
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")
    .create();

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
        Context context = CircleApp.getInstance().getApplicationContext();
        Toast.makeText(context, context.getResources().getString(R.string.refreshing), Toast.LENGTH_LONG).show();
        api.listBuilds(new Callback<Object>() {
            @Override
            public void success(Object builds, Response response) {
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

    @Subscribe
    public void retryBuild(RetryBuildEvent event) {
        api.retryBuild(event.getProject(), event.getUsername(), event.getBuildNumber(), new Callback<Object>() {
            @Override
            public void success(Object object, Response response) {
                Build build = gson.fromJson(gson.toJson(object), Build.class);
                bus.post(new RetrySuccessfulEvent(build));
            }

            @Override
            public void failure(RetrofitError error) {
                bus.post(new ApiErrorEvent(error));
            }
        });
    }

    @Subscribe
    public void cancelBuild(CancelBuildEvent event) {
        api.cancelBuild(event.getProject(), event.getUsername(), event.getBuildNumber(), new Callback<Object>() {
            @Override
            public void success(Object object, Response response) {
                Build build = gson.fromJson(gson.toJson(object), Build.class);
                bus.post(new CancelBuildSuccessful(build));
            }

            @Override
            public void failure(RetrofitError error) {
                bus.post(new ApiErrorEvent(error));
            }
        });
    }
}
