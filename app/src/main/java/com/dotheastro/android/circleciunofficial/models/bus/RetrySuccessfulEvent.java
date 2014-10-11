package com.dotheastro.android.circleciunofficial.models.bus;

import com.dotheastro.android.circleciunofficial.models.Build;

/**
 * Created by Siena on 10/11/2014.
 */
public class RetrySuccessfulEvent {
    private Build build;

    public RetrySuccessfulEvent(Build build) {
        this.build = build;
    }
}
