package com.dotheastro.android.circleciunofficial.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.dotheastro.android.circleciunofficial.BR;
import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.Build;
import com.dotheastro.android.circleciunofficial.models.Handlers;
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
  private Context context;
  private Resources res;
  private String packageName;
  private Build[] builds;

  public BuildsAdapter(Context context, Build[] builds) {
    this.context = context;
    this.res = context.getResources();
    this.packageName = context.getPackageName();
    this.builds = builds;
  }

  public void setBuilds(Build[] builds) {
    this.builds = builds;
    notifyDataSetChanged();
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, int position) {
    Build build = builds[position];
    viewHolder.binding.setVariable(BR.build, build);
    viewHolder.binding.setVariable(BR.handlers, new Handlers());

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
      viewHolder.status.setBackgroundColor(ContextCompat.getColor(context, statusColorId));
    } else {
      viewHolder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.canceled));
    }
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

  static class ViewHolder extends RecyclerView.ViewHolder {
    public ViewDataBinding binding;

    public TextView startedAt;
    public TextView length;
    public TextView status;

    public ViewHolder(View rowView) {
      super(rowView);

      binding = DataBindingUtil.bind(rowView);

      startedAt = (TextView) rowView.findViewById(R.id.started_at);
      length = (TextView) rowView.findViewById(R.id.length);
      status = (TextView) rowView.findViewById(R.id.status);
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
