package com.dotheastro.android.circleciunofficial.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.adapters.BuildsAdapter;
import com.dotheastro.android.circleciunofficial.models.Build;
import com.dotheastro.android.circleciunofficial.models.bus.ApiErrorEvent;
import com.dotheastro.android.circleciunofficial.models.bus.BuildsLoadedEvent;
import com.dotheastro.android.circleciunofficial.models.bus.BusProvider;
import com.dotheastro.android.circleciunofficial.models.bus.CancelBuildSuccessful;
import com.dotheastro.android.circleciunofficial.models.bus.LoadBuildsEvent;
import com.dotheastro.android.circleciunofficial.models.bus.RetrySuccessfulEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;


public class BuildsActivity extends Activity {

    private List<Build> builds;
    private BuildsAdapter adapter;
    private Bus bus;
    private LinearLayout getStarted;
    private ListView listView;

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_builds);
        setUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBus().register(this);
    }

    private boolean tokenIsEmpty() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String apiToken = prefs.getString("circleAPIToken", null);
        return apiToken == null || apiToken.equals("");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if ( tokenIsEmpty() ) {
            listView.setVisibility(View.GONE);
            getStarted.setVisibility(View.VISIBLE);
        } else {
            getStarted.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            getBus().post(new LoadBuildsEvent());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        getBus().unregister(this);
    }

    @Subscribe
    public void updateViewWithBuilds(BuildsLoadedEvent event) {
        adapter.clear();
        adapter.addAll(event.getBuilds());
    }

    @Subscribe
    public void onSuccessfulRetry(RetrySuccessfulEvent event) {
        Toast.makeText(this, getString(R.string.retry_successful), Toast.LENGTH_LONG).show();
        getBus().post(new LoadBuildsEvent());
    }

    @Subscribe
    public void onSuccessfulCancel(CancelBuildSuccessful event) {
        Toast.makeText(this, getString(R.string.cancel_successful), Toast.LENGTH_LONG).show();
        getBus().post(new LoadBuildsEvent());
    }

    @Subscribe
    public void onApiError(ApiErrorEvent event) {
        adapter.clear();
        getStarted.setVisibility(View.VISIBLE);
    }

    public void setUp() {
        // set list adapter
        getStarted = (LinearLayout)findViewById(R.id.get_started);
        listView = (ListView)findViewById(R.id.builds_list);
        adapter = new BuildsAdapter(this, R.layout.partial_build);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.builds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, PreferencesActivity.class));
            return true;
        } else if (id == R.id.refresh && !tokenIsEmpty()) {
            getBus().post(new LoadBuildsEvent());
        }
        return super.onOptionsItemSelected(item);
    }

    private Bus getBus() {
        if (bus == null) {
            bus = BusProvider.getInstance();
        }
        return bus;
    }
}
