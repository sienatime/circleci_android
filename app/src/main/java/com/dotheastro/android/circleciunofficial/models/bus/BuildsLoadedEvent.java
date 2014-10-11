package com.dotheastro.android.circleciunofficial.models.bus;

import com.dotheastro.android.circleciunofficial.models.Build;

/**
 * Created by Siena on 10/11/2014.
 */
public class BuildsLoadedEvent {
    private Build[] builds;

    public BuildsLoadedEvent(Build[] builds) {
        this.builds = builds;
    }

    public Build[] getBuilds() {
        return builds;
    }
}
