package net.emojiparty.android.circleciunofficial.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.emojiparty.android.circleciunofficial.BR;
import com.emojiparty.android.circleciunofficial.R;
import net.emojiparty.android.circleciunofficial.adapters.BuildsAdapter;
import com.emojiparty.android.circleciunofficial.databinding.ActivityBuildsBinding;
import net.emojiparty.android.circleciunofficial.models.Build;
import net.emojiparty.android.circleciunofficial.models.bus.ApiErrorEvent;
import net.emojiparty.android.circleciunofficial.models.bus.BuildsLoadedEvent;
import net.emojiparty.android.circleciunofficial.models.bus.BusProvider;
import net.emojiparty.android.circleciunofficial.models.bus.CancelBuildSuccessful;
import net.emojiparty.android.circleciunofficial.models.bus.LoadBuildsEvent;
import net.emojiparty.android.circleciunofficial.models.bus.RetrySuccessfulEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BuildsActivity extends AppCompatActivity {
  private BuildsAdapter adapter;
  private Bus bus;
  private LinearLayout getStarted;
  private RecyclerView recyclerView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ActivityBuildsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_builds);
    binding.setVariable(BR.activity, this);
    setUpRecyclerView();
  }

  @Override protected void onStart() {
    super.onStart();
    getBus().register(this);
  }

  private boolean tokenIsEmpty() {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
    String apiToken = prefs.getString("circleAPIToken", null);
    return apiToken == null || apiToken.equals("");
  }

  @Override protected void onResume() {
    super.onResume();

    if (tokenIsEmpty()) {
      adapter.clear();
      getStarted.setVisibility(View.VISIBLE);
    } else {
      getStarted.setVisibility(View.GONE);
      getBus().post(new LoadBuildsEvent());
    }
  }

  @Override protected void onStop() {
    super.onStop();
    getBus().unregister(this);
  }

  @Subscribe public void updateViewWithBuilds(BuildsLoadedEvent event) {
    adapter.setBuilds(event.getBuilds());
  }

  @Subscribe public void onSuccessfulRetry(RetrySuccessfulEvent event) {
    Toast.makeText(this, getString(R.string.retry_successful), Toast.LENGTH_SHORT).show();
    getBus().post(new LoadBuildsEvent());
  }

  @Subscribe public void onSuccessfulCancel(CancelBuildSuccessful event) {
    Toast.makeText(this, getString(R.string.cancel_successful), Toast.LENGTH_SHORT).show();
    getBus().post(new LoadBuildsEvent());
  }

  @Subscribe public void onApiError(ApiErrorEvent event) {
    getStarted.setVisibility(View.VISIBLE);
    adapter.clear();
  }

  public void setUpRecyclerView() {
    // set list adapter
    getStarted = (LinearLayout) findViewById(R.id.get_started);
    recyclerView = (RecyclerView) findViewById(R.id.builds_list);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    adapter = new BuildsAdapter(this, new Build[0]);
    recyclerView.setAdapter(adapter);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.builds, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
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

  public void openCircleAPITokenSettings(View view) {
    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://circleci.com/account/api"));
    startActivity(browserIntent);
  }
}
