package com.dotheastro.android.circleciunofficial.models.bus;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.CircleApp;

import retrofit.RetrofitError;

/**
 * Created by Siena on 10/11/2014.
 */
public class ApiErrorEvent {
    private RetrofitError error;
    private String message;

    public ApiErrorEvent(RetrofitError error) {
        this.error = error;
        this.message = CircleApp.getInstance().getString(R.string.api_error) + this.error.toString();
    }

    public String getErrorToString(){
        return this.error.toString();
    }

    public RetrofitError getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
