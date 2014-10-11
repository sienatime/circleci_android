package com.dotheastro.android.circleciunofficial.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.adapters.BuildsAdapter;
import com.dotheastro.android.circleciunofficial.models.Build;
import com.dotheastro.android.circleciunofficial.models.bus.BuildsLoadedEvent;
import com.dotheastro.android.circleciunofficial.models.bus.BusProvider;
import com.dotheastro.android.circleciunofficial.models.bus.LoadBuildsEvent;
import com.dotheastro.android.circleciunofficial.models.bus.RetrySuccessfulEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Arrays;
import java.util.List;


public class BuildsActivity extends Activity {

    private List<Build> builds;
    private BuildsAdapter adapter;
    private Bus bus;

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        getBus().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBus().post(new LoadBuildsEvent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        getBus().unregister(this);
    }

    @Subscribe
    public void updateViewWithBuilds(BuildsLoadedEvent event) {
        setBuilds(Arrays.asList(event.getBuilds()));
        setUpView();
    }

    @Subscribe
    public void onSuccessfulRetry(RetrySuccessfulEvent event) {
        Toast.makeText(this, getString(R.string.retry_successful), Toast.LENGTH_LONG).show();
    }

    public void setUpView() {
        // set list adapter
        adapter = new BuildsAdapter(this, R.layout.partial_build, builds);
        ListView listView = (ListView)findViewById(R.id.builds_list);
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
