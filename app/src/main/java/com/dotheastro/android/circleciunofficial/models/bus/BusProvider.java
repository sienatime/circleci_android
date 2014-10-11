package com.dotheastro.android.circleciunofficial.models.bus;

import com.squareup.otto.Bus;

/**
 * Created by Siena on 10/11/2014.
 */
public final class BusProvider {
    private static Bus instance = null;
    protected BusProvider() {
        // Exists only to defeat instantiation.
    }
    public static Bus getInstance() {
        if(instance == null) {
            instance = new Bus();
        }
        return instance;
    }
}
