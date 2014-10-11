package com.dotheastro.android.circleciunofficial.interfaces;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Siena on 9/5/2014.
 */
public interface CircleAPI {

    //https://circleci.com/api/v1/recent-builds?circle-token=:token&limit=20&offset=
    @Headers("Accept: application/json")
    @GET("/api/v1/recent-builds")
    void listBuilds(Callback<Object> callback);

    @Headers("Accept: application/json")
    @POST("/api/v1/project/{username}/{project}/{buildNumber}/retry")
    void retryBuild(@Path("username") String username, @Path("project") String project, @Path("buildNumber") int buildNumber, Callback<Object> callback);
}
