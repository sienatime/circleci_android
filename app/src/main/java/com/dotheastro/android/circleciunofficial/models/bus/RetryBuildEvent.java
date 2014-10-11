package com.dotheastro.android.circleciunofficial.models.bus;

/**
 * Created by Siena on 10/11/2014.
 */
public class RetryBuildEvent {
    private String username;
    private String project;
    private int buildNumber;

    public RetryBuildEvent(String username, String project, int buildNumber) {
        this.username = username;
        this.project = project;
        this.buildNumber = buildNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getProject() {
        return project;
    }

    public int getBuildNumber() {
        return buildNumber;
    }
}
