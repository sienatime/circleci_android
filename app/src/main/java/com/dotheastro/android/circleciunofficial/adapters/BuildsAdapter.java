package com.dotheastro.android.circleciunofficial.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.Build;
import com.dotheastro.android.circleciunofficial.models.bus.BusProvider;
import com.dotheastro.android.circleciunofficial.models.bus.CancelBuildEvent;
import com.dotheastro.android.circleciunofficial.models.bus.RetryBuildEvent;
import com.squareup.otto.Bus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.ocpsoft.prettytime.PrettyTime;

/**
 * Created by Siena on 9/6/2014.
 */
public class BuildsAdapter extends RecyclerView.Adapter<BuildsAdapter.ViewHolder> {

  final static PrettyTime prettyTime = new PrettyTime();
  private Bus bus;
  private Resources res;
  private String packageName;
  private Build[] builds;

  public BuildsAdapter(Context context, Build[] builds) {
    this.res = context.getResources();
    this.packageName = context.getPackageName();
    this.builds = builds;
  }

  public void setBuilds(Build[] builds) {
    this.builds = builds;
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, int position) {
    Build build = builds[position];

    String buildTitle =
        String.format(res.getString(R.string.build_title), build.reponame, build.branch,
            Integer.toString(build.build_num));
    viewHolder.buildTitle.setText(buildTitle);

    String authorName = build.committer_name == null ? "" : build.committer_name;

    viewHolder.author.setText(String.format(res.getString(R.string.author), authorName));
    viewHolder.log.setText(build.subject);

    if (build.start_time != null) {
      Date localTime = new Date(
          build.start_time.getTime() + TimeZone.getDefault().getOffset(build.start_time.getTime()));
      viewHolder.startedAt.setText(
          String.format(res.getString(R.string.started), prettyTime.format(localTime)));
    } else {
      viewHolder.startedAt.setText(R.string.not_started);
    }

    viewHolder.length.setText(
        String.format(res.getString(R.string.duration), millisToString(build.build_time_millis)));

    int statusStringId = res.getIdentifier(build.status, "string", packageName);

    if (statusStringId != 0) {
      viewHolder.status.setText(res.getString(statusStringId));
    } else {
      viewHolder.status.setText(build.status);
    }

    int statusColorId = res.getIdentifier(build.status, "color", packageName);

    if (statusColorId != 0) {
      viewHolder.status.setBackgroundColor(res.getColor(statusColorId));
    } else {
      viewHolder.status.setBackgroundColor(res.getColor(R.color.canceled));
    }

    if (build.status.equals("running")) {
      viewHolder.cancel.setVisibility(View.VISIBLE);
    } else {
      viewHolder.cancel.setVisibility(View.GONE);
    }

    viewHolder.retry.setOnClickListener(
        retryBuildListener(build.reponame, build.username, build.build_num));
    viewHolder.cancel.setOnClickListener(
        cancelBuildListener(build.reponame, build.username, build.build_num));
  }

  @Override public int getItemCount() {
    return builds.length;
  }

  @Override public BuildsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View parentLayout =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.partial_build, parent, false);
    ViewHolder viewHolder = new ViewHolder(parentLayout);
    return viewHolder;
  }

  private Bus getBus() {
    if (bus == null) {
      bus = BusProvider.getInstance();
    }
    return bus;
  }

  private Button.OnClickListener retryBuildListener(final String project, final String username,
      final int buildNumber) {
    return new Button.OnClickListener() {
      @Override public void onClick(View view) {
        view.setEnabled(false);
        getBus().post(new RetryBuildEvent(project, username, buildNumber));
      }
    };
  }

  private Button.OnClickListener cancelBuildListener(final String project, final String username,
      final int buildNumber) {
    return new Button.OnClickListener() {
      @Override public void onClick(View view) {
        view.setEnabled(false);
        getBus().post(new CancelBuildEvent(project, username, buildNumber));
      }
    };
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    public TextView buildTitle;
    public TextView author;
    public TextView log;
    public TextView startedAt;
    public TextView length;
    public TextView status;
    public Button retry;
    public Button cancel;

    public ViewHolder(View parentLayout) {
      super(parentLayout);

      buildTitle = (TextView) parentLayout.findViewById(R.id.build_title);
      author = (TextView) parentLayout.findViewById(R.id.author);
      log = (TextView) parentLayout.findViewById(R.id.log);
      startedAt = (TextView) parentLayout.findViewById(R.id.started_at);
      length = (TextView) parentLayout.findViewById(R.id.length);
      status = (TextView) parentLayout.findViewById(R.id.status);
      retry = (Button) parentLayout.findViewById(R.id.retry);
      cancel = (Button) parentLayout.findViewById(R.id.cancel);
    }
  }

  public String millisToString(long ms) {
    DateFormat df = new SimpleDateFormat("mm:ss");
    if (ms > 1000 * 60 * 60) {
      df = new SimpleDateFormat("H:mm:ss");
    }
    return df.format(ms);
  }
}
