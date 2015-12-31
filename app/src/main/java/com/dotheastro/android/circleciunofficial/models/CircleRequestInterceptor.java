package com.dotheastro.android.circleciunofficial.models;

/**
 * Created by Siena on 10/11/2014.
 */
public class CircleRequestInterceptor implements retrofit.RequestInterceptor {
  private String token;

  public CircleRequestInterceptor(String token) {
    this.token = token;
  }

  @Override public void intercept(RequestFacade request) {
    // put API token into every request
    request.addQueryParam("circle-token", token);
  }
}
