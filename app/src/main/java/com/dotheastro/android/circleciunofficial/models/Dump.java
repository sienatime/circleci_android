package com.dotheastro.android.circleciunofficial.models;

import android.content.res.Resources;

import com.dotheastro.android.circleciunofficial.R;

import java.util.HashMap;

/**
 * Created by Siena on 10/11/2014.
 */
public class Dump {
    private static HashMap<String, Integer> statusColorMap;
    private static HashMap<String, String> localizedStatusMap;

    public static HashMap<String, Integer> getStatusColorMap(Resources res) {
        if (statusColorMap == null) {
            statusColorMap = new HashMap<String, Integer>();
            statusColorMap.put("failed", res.getColor(R.color.failed));
            statusColorMap.put("not_run", res.getColor(R.color.not_run));
            statusColorMap.put("canceled", res.getColor(R.color.canceled));
            statusColorMap.put("running", res.getColor(R.color.running));
            statusColorMap.put("success", res.getColor(R.color.happy));
        }
        return statusColorMap;
    }

    public static HashMap<String, String> getLocalizedStatusString(Resources res) {
        if (localizedStatusMap == null) {
            localizedStatusMap = new HashMap<String, String>();
            localizedStatusMap.put("failed", res.getString(R.string.failed));
            localizedStatusMap.put("not_run", res.getString(R.string.not_run));
            localizedStatusMap.put("success", res.getString(R.string.success));
            localizedStatusMap.put("running", res.getString(R.string.running));
            localizedStatusMap.put("canceled", res.getString(R.string.canceled));
        }
        return localizedStatusMap;
    }
}
