package com.dotheastro.android.circleciunofficial.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dotheastro.android.circleciunofficial.R;
import com.dotheastro.android.circleciunofficial.models.Build;
import com.dotheastro.android.circleciunofficial.models.bus.BusProvider;
import com.dotheastro.android.circleciunofficial.models.bus.RetryBuildEvent;
import com.squareup.otto.Bus;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.List;

/**
 * Created by Siena on 9/6/2014.
 */
public class BuildsAdapter extends ArrayAdapter<Build> {

    final static PrettyTime prettyTime = new PrettyTime();
    private Bus bus;

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
                getBus().post(new RetryBuildEvent(project, username, buildNumber));
            }
        };
    }

    static class ViewHolder {
        public TextView repoName;
        public TextView buildNum;
        public TextView branch;
        public TextView author;
        public TextView log;
        public TextView startedAt;
        public TextView length;
        public TextView status;
        public Button retry;

        ViewHolder(TextView repoName, TextView buildNum, TextView branch, TextView author, TextView log, TextView startedAt, TextView length, TextView status, Button retry) {
            this.repoName = repoName;
            this.buildNum = buildNum;
            this.branch = branch;
            this.author = author;
            this.log = log;
            this.startedAt = startedAt;
            this.length = length;
            this.status = status;
            this.retry = retry;
        }
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Build build = getItem(position);

        ViewHolder viewHolder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.partial_build, parent, false);

            final TextView repoName = (TextView)convertView.findViewById(R.id.reponame);
            final TextView buildNum = (TextView) convertView.findViewById(R.id.build_num);
            final TextView branch = (TextView) convertView.findViewById(R.id.branch);
            final TextView author = (TextView) convertView.findViewById(R.id.author);
            final TextView log = (TextView) convertView.findViewById(R.id.log);
            final TextView startedAt = (TextView) convertView.findViewById(R.id.started_at);
            final TextView length = (TextView) convertView.findViewById(R.id.length);
            final TextView status = (TextView) convertView.findViewById(R.id.status);
            final Button retry = (Button) convertView.findViewById(R.id.retry);

            viewHolder = new ViewHolder(repoName, buildNum, branch, author, log, startedAt, length, status, retry);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        viewHolder.repoName.setText(build.reponame);
        viewHolder.buildNum.setText(Integer.toString(build.build_num));
        viewHolder.branch.setText(build.branch);
        viewHolder.author.setText(build.committer_name);
        viewHolder.log.setText(build.subject);

        if (build.start_time != null) {
            viewHolder.startedAt.setText(prettyTime.format(build.start_time));
        }

        viewHolder.length.setText(String.format(getContext().getResources().getString(R.string.millis),
                Long.toString(build.build_time_millis)));

        viewHolder.status.setText(build.status);
        viewHolder.retry.setOnClickListener(retryBuildListener(build.reponame, build.username, build.build_num));

        return convertView;
    }

    public BuildsAdapter(Context context, int resource, List<Build> objects) {
        super(context, resource, objects);
    }
}
