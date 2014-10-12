package com.dotheastro.android.circleciunofficial.models.bus;

import com.dotheastro.android.circleciunofficial.models.Build;

/**
 * Created by Siena on 10/11/2014.
 */
public class CancelBuildSuccessful {

    private Build build;

    public CancelBuildSuccessful(Build build) {
        this.build = build;
    }

    public Build getBuild() {
        return build;
    }
}
