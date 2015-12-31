package net.emojiparty.android.circleciunofficial.models;

import android.view.View;
import net.emojiparty.android.circleciunofficial.models.bus.BusProvider;
import net.emojiparty.android.circleciunofficial.models.bus.CancelBuildEvent;
import net.emojiparty.android.circleciunofficial.models.bus.RetryBuildEvent;
import com.squareup.otto.Bus;

/**
 * Created by Siena Aguayo on 12/30/15.
 */
public class Handlers {
  private Bus bus;

  public Handlers() {
  }

  private Bus getBus() {
    if (bus == null) {
      bus = BusProvider.getInstance();
    }
    return bus;
  }

  public View.OnClickListener onClickRetry(final String project, final String username,
      final int buildNumber) {
    return new View.OnClickListener() {
      @Override public void onClick(View view) {
        getBus().post(new RetryBuildEvent(project, username, buildNumber));
      }
    };
  }

  public View.OnClickListener onClickCancel(final String project, final String username,
      final int buildNumber) {
    return new View.OnClickListener() {
      @Override public void onClick(View view) {
        getBus().post(new CancelBuildEvent(project, username, buildNumber));
      }
    };
  }
}
