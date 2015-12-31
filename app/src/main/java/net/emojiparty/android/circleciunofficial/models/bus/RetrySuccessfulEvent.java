package net.emojiparty.android.circleciunofficial.models.bus;

import net.emojiparty.android.circleciunofficial.models.Build;

/**
 * Created by Siena on 10/11/2014.
 */
public class RetrySuccessfulEvent {
    private Build build;

    public RetrySuccessfulEvent(Build build) {
        this.build = build;
    }
}
