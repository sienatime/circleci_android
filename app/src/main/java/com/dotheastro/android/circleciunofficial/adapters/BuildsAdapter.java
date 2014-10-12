package com.dotheastro.android.circleciunofficial.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.Build;
import com.dotheastro.android.circleciunofficial.models.Dump;
import com.dotheastro.android.circleciunofficial.models.bus.BusProvider;
import com.dotheastro.android.circleciunofficial.models.bus.CancelBuildEvent;
import com.dotheastro.android.circleciunofficial.models.bus.RetryBuildEvent;
import com.squareup.otto.Bus;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Siena on 9/6/2014.
 */
public class BuildsAdapter extends ArrayAdapter<Build> {

    final static PrettyTime prettyTime = new PrettyTime();
    private Bus bus;
    private Resources res;
    private HashMap<String, Integer> statusColorMap;
    private HashMap<String, String> localizedStatusMap;

    private Bus getBus() {
        if (bus == null) {
            bus = BusProvider.getInstance();
        }
        return bus;
    }

    private Button.OnClickListener retryBuildListener(final String project, final String username, final int buildNumber) {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                getBus().post(new RetryBuildEvent(project, username, buildNumber));
            }
        };
    }

    private Button.OnClickListener cancelBuildListener(final String project, final String username, final int buildNumber) {
        return new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                getBus().post(new CancelBuildEvent(project, username, buildNumber));
            }
        };
    }

    static class ViewHolder {
        public TextView buildTitle;
        public TextView author;
        public TextView log;
        public TextView startedAt;
        public TextView length;
        public TextView status;
        public Button retry;
        public Button cancel;

        ViewHolder(TextView buildTitle, TextView author, TextView log, TextView startedAt, TextView length, TextView status, Button retry, Button cancel) {
            this.buildTitle = buildTitle;
            this.author = author;
            this.log = log;
            this.startedAt = startedAt;
            this.length = length;
            this.status = status;
            this.retry = retry;
            this.cancel = cancel;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Build build = getItem(position);

        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.partial_build, parent, false);

            final TextView buildTitle = (TextView) convertView.findViewById(R.id.build_title);
            final TextView author = (TextView) convertView.findViewById(R.id.author);
            final TextView log = (TextView) convertView.findViewById(R.id.log);
            final TextView startedAt = (TextView) convertView.findViewById(R.id.started_at);
            final TextView length = (TextView) convertView.findViewById(R.id.length);
            final TextView status = (TextView) convertView.findViewById(R.id.status);
            final Button retry = (Button) convertView.findViewById(R.id.retry);
            final Button cancel = (Button) convertView.findViewById(R.id.cancel);

            viewHolder = new ViewHolder(buildTitle, author, log, startedAt, length, status, retry, cancel);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        String buildTitle = String.format(res.getString(R.string.build_title), build.reponame, build.branch, Integer.toString(build.build_num));
        viewHolder.buildTitle.setText(buildTitle);

        String authorName = build.committer_name == null ? "" : build.committer_name;

        viewHolder.author.setText(String.format(res.getString(R.string.author), authorName));
        viewHolder.log.setText(build.subject);

        if (build.start_time != null) {
            viewHolder.startedAt.setText(String.format(res.getString(R.string.started), prettyTime.format(build.start_time)));
        } else {
            viewHolder.startedAt.setText(R.string.not_started);
        }

        viewHolder.length.setText(String.format(res.getString(R.string.duration), millisToString(build.build_time_millis)));

        viewHolder.status.setText(localizedStatusMap.get(build.status));
        viewHolder.status.setBackgroundColor(statusColorMap.get(build.status));

        if (build.status.equals("running")) {
            viewHolder.cancel.setVisibility(View.VISIBLE);
        } else {
            viewHolder.cancel.setVisibility(View.GONE);
        }

        viewHolder.retry.setOnClickListener(retryBuildListener(build.reponame, build.username, build.build_num));
        viewHolder.cancel.setOnClickListener(cancelBuildListener(build.reponame, build.username, build.build_num));

        return convertView;
    }

    public BuildsAdapter(Context context, int resource) {
        super(context, resource);
        this.res = context.getResources();
        this.statusColorMap = Dump.getStatusColorMap(this.res);
        this.localizedStatusMap = Dump.getLocalizedStatusString(this.res);
    }

    public String millisToString(long ms) {
        DateFormat df = new SimpleDateFormat("mm:ss");
        if (ms > 1000 * 60 * 60) {
            df = new SimpleDateFormat("H:mm:ss");
        }
        return df.format(ms);
    }
}
